package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.sandboxpoc.hostruntime.capability.CapabilityState

/**
 * Programmatic-UI helpers and the app's small visual system (light theme).
 *
 * No XML layouts, no dependencies — plain views only, so the manual
 * kotlinc/aapt2 build keeps working. Activities compose screens from:
 * [screen]/[page], [title]/[subtitle]/[section]/[footnote], [card] +
 * [cardHeader]/[cardTitle], [kvRow], [pill]/[capabilityPill]/[statePill],
 * [button]/[secondaryButton]/[dangerButton], [collapsible].
 */
object Ui {

    // -- Palette (light theme, explicit colors) ---------------------------
    // Plain vals (not const): 0xFFxxxxxx literals exceed Int range, so they
    // are Long until .toInt() — not a constant expression.
    val PRIMARY = 0xFF1A73E8.toInt()
    val TEXT = 0xFF1F1F1F.toInt()
    val TEXT_2 = 0xFF5F6368.toInt()
    val RED = 0xFFD93025.toInt()
    val GREEN = 0xFF1E8E3E.toInt()

    private const val C_BG = 0xFFF6F7F9.toInt()
    private const val C_CARD = 0xFFFFFFFF.toInt()
    private const val C_BORDER = 0xFFE1E4E8.toInt()
    private const val C_GREEN_BG = 0xFFE6F4EA.toInt()
    private const val C_AMBER = 0xFFB06000.toInt()
    private const val C_AMBER_BG = 0xFFFEF7E0.toInt()
    private const val C_BLUE_BG = 0xFFE8F0FE.toInt()
    private const val C_RED_BG = 0xFFFCE8E6.toInt()
    private const val C_GRAY_BG = 0xFFF1F3F4.toInt()
    private const val C_WHITE = 0xFFFFFFFF.toInt()

    /**
     * Run [work] on a background thread, then [onDone] on the UI thread.
     * Engine calls do Binder IPC and must never run on the main thread
     * (the v2 prototype ANR'd on exactly this).
     */
    fun bg(activity: Activity, work: () -> String, onDone: (String) -> Unit = {}) {
        Thread {
            // Throwable, not just Exception: an Error (e.g. stack overflow
            // in a deep delete) must surface as a FAIL toast, never as
            // silence — a dead thread with no toast looks like "nothing
            // happens" when tapping Delete/Reset.
            val result = try { work() } catch (t: Throwable) {
                "FAIL: ${t.javaClass.simpleName}: ${t.message ?: t}"
            }
            activity.runOnUiThread {
                Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
                onDone(result)
            }
        }.start()
    }

    fun screen(context: Context): LinearLayout {
        val scroll = ScrollView(context)
        scroll.setBackgroundColor(C_BG)
        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            val p = dp(context, 16)
            setPadding(p, p, p, p)
        }
        scroll.addView(root, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        // Activities call setContentView(Ui.page(root)) to get root + scroll.
        return root
    }

    /** Wraps [Ui.screen] output in its ScrollView for setContentView. */
    fun page(root: LinearLayout): View = root.parent as View

    // -- Typography -------------------------------------------------------

    fun title(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 22f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(TEXT)
            setPadding(0, 0, 0, dp(context, 2))
        }

    fun subtitle(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 13f
            setTextColor(TEXT_2)
            setPadding(0, 0, 0, dp(context, 4))
        }

    fun footnote(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 12f
            setTextColor(TEXT_2)
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, dp(context, 16), 0, dp(context, 4))
        }

    fun section(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 16f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(TEXT)
            setPadding(0, dp(context, 20), 0, dp(context, 8))
        }

    fun row(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 14f
            setTextColor(TEXT)
            setPadding(0, dp(context, 3), 0, dp(context, 3))
        }

    fun mono(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 12f
            typeface = Typeface.MONOSPACE
            setTextColor(TEXT)
            setPadding(0, dp(context, 2), 0, dp(context, 2))
        }

    // -- Cards ------------------------------------------------------------

    /** Rounded white card: 12dp radius, 1dp border, 16dp padding, 8dp margins. */
    fun card(context: Context, fill: Int = C_CARD): LinearLayout =
        LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            val p = dp(context, 16)
            setPadding(p, p, p, p)
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = dp(context, 12).toFloat()
                setColor(fill)
                setStroke(dp(context, 1), C_BORDER)
            }
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = dp(context, 8); bottomMargin = dp(context, 8)
            }
        }

    /** Light-red card for destructive actions. */
    fun dangerCard(context: Context): LinearLayout = card(context, C_RED_BG)

    fun cardTitle(context: Context, text: String, color: Int = TEXT): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 16f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(color)
            setPadding(0, 0, 0, dp(context, 8))
        }

    /** Card header: bold title left, optional badge (e.g. a pill) right. */
    fun cardHeader(context: Context, title: String, badge: View? = null): LinearLayout {
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, 0, 0, dp(context, 8))
        }
        row.addView(TextView(context).apply {
            text = title
            textSize = 16f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(TEXT)
            layoutParams = LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        })
        if (badge != null) row.addView(badge)
        return row
    }

    /** Two-column key/value row for cards: label left (secondary), value right. */
    fun kvRow(context: Context, key: String, value: String): LinearLayout {
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, dp(context, 3), 0, dp(context, 3))
        }
        row.addView(TextView(context).apply {
            text = key
            textSize = 13f
            setTextColor(TEXT_2)
            layoutParams = LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.42f)
        })
        row.addView(TextView(context).apply {
            text = value
            textSize = 13f
            setTextColor(TEXT)
            layoutParams = LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.58f)
        })
        return row
    }

    // -- Pills (status badges) --------------------------------------------

    fun pill(context: Context, text: String, bg: Int, fg: Int): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 11f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(fg)
            val h = dp(context, 10)
            setPadding(h, dp(context, 4), h, dp(context, 4))
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = dp(context, 10).toFloat()
                setColor(bg)
            }
        }

    /** Green for ok-ish states, red otherwise. */
    fun statePill(context: Context, text: String, ok: Boolean): TextView =
        pill(context, text,
            if (ok) C_GREEN_BG else C_RED_BG,
            if (ok) GREEN else RED)

    fun capabilityPill(context: Context, state: CapabilityState): TextView =
        when (state) {
            CapabilityState.SUPPORTED ->
                pill(context, "Supported", C_GREEN_BG, GREEN)
            CapabilityState.PARTIALLY_SUPPORTED ->
                pill(context, "Partial", C_AMBER_BG, C_AMBER)
            CapabilityState.EXPERIMENTAL ->
                pill(context, "Experimental", C_BLUE_BG, PRIMARY)
            CapabilityState.UNSUPPORTED ->
                pill(context, "Unsupported", C_RED_BG, RED)
            CapabilityState.HOST_PROVIDED ->
                pill(context, "Host", C_GRAY_BG, TEXT_2)
            CapabilityState.UNVERIFIED ->
                pill(context, "Unverified", C_AMBER_BG, C_AMBER)
        }

    // -- Buttons ----------------------------------------------------------

    private fun styledButton(
        context: Context, text: String, fill: Int, fg: Int, stroke: Int,
        weight: Float, onClick: () -> Unit,
    ): Button = Button(context).apply {
        this.text = text
        isAllCaps = false
        textSize = 14f
        setTypeface(typeface, Typeface.BOLD)
        setTextColor(fg)
        setPadding(dp(context, 12), dp(context, 10), dp(context, 12), dp(context, 10))
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dp(context, 8).toFloat()
            setColor(fill)
            if (stroke != 0) setStroke(dp(context, 1), stroke)
        }
        layoutParams = if (weight > 0f) {
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, weight).apply {
                topMargin = dp(context, 6)
                leftMargin = dp(context, 4); rightMargin = dp(context, 4)
            }
        } else {
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = dp(context, 6)
            }
        }
        setOnClickListener { onClick() }
    }

    /** Primary filled button. Pass [weight] > 0 for side-by-side buttons. */
    fun button(context: Context, text: String, weight: Float = 0f,
               onClick: () -> Unit): Button =
        styledButton(context, text, PRIMARY, C_WHITE, 0, weight, onClick)

    /** Outlined secondary button. */
    fun secondaryButton(context: Context, text: String, weight: Float = 0f,
                        onClick: () -> Unit): Button =
        styledButton(context, text, C_CARD, PRIMARY, PRIMARY, weight, onClick)

    /** Red button for destructive actions. */
    fun dangerButton(context: Context, text: String, weight: Float = 0f,
                     onClick: () -> Unit): Button =
        styledButton(context, text, RED, C_WHITE, 0, weight, onClick)

    /** Horizontal row holding weighted buttons side-by-side. */
    fun buttonRow(context: Context, vararg buttons: Button): LinearLayout {
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, dp(context, 2), 0, 0)
        }
        buttons.forEach { row.addView(it) }
        return row
    }

    // -- Collapsible ------------------------------------------------------

    /**
     * A secondary toggle button ("▸ title" / "▾ title") showing/hiding a
     * container built by [buildContent]. Used for technical dumps and logs.
     */
    fun collapsible(context: Context, title: String, initiallyOpen: Boolean = false,
                    buildContent: (LinearLayout) -> Unit): LinearLayout {
        val outer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        val content = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            visibility = if (initiallyOpen) View.VISIBLE else View.GONE
        }
        buildContent(content)
        var open = initiallyOpen
        fun label() = (if (open) "▾ " else "▸ ") + title
        lateinit var toggle: Button
        toggle = secondaryButton(context, label()) {
            open = !open
            content.visibility = if (open) View.VISIBLE else View.GONE
            toggle.text = label()
        }
        outer.addView(toggle)
        outer.addView(content)
        return outer
    }

    // -- Misc -------------------------------------------------------------

    fun searchField(context: Context, hint: String): EditText =
        EditText(context).apply {
            this.hint = hint
            inputType = InputType.TYPE_CLASS_TEXT
            setSingleLine()
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = dp(context, 8).toFloat()
                setColor(C_CARD)
                setStroke(dp(context, 1), C_BORDER)
            }
            val h = dp(context, 12)
            setPadding(h, dp(context, 10), h, dp(context, 10))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = dp(context, 4); bottomMargin = dp(context, 4)
            }
        }

    fun divider(context: Context): View =
        View(context).apply {
            setBackgroundColor(C_BORDER)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(context, 1)).apply {
                topMargin = dp(context, 8); bottomMargin = dp(context, 8)
            }
        }

    fun dp(context: Context, v: Int): Int =
        (v * context.resources.displayMetrics.density).toInt()
}
