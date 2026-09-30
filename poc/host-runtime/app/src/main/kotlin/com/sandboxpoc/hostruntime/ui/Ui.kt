package com.sandboxpoc.hostruntime.ui

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

/** Minimal programmatic-UI helpers. Functional only — no visual polish. */
object Ui {

    /**
     * Run [work] on a background thread, then [onDone] on the UI thread.
     * Engine calls do Binder IPC and must never run on the main thread
     * (the v2 prototype ANR'd on exactly this).
     */
    fun bg(activity: Activity, work: () -> String, onDone: (String) -> Unit = {}) {
        Thread {
            val result = try { work() } catch (e: Exception) { "FAIL: ${e.message ?: e}" }
            activity.runOnUiThread {
                Toast.makeText(activity, result, Toast.LENGTH_LONG).show()
                onDone(result)
            }
        }.start()
    }

    fun screen(context: Context): LinearLayout {
        val scroll = ScrollView(context)
        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            val p = dp(context, 16)
            setPadding(p, p, p, p)
        }
        scroll.addView(root, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        // Activities call setContentView(Ui.page(context)) to get root + scroll.
        return root
    }

    /** Wraps [Ui.screen] output in its ScrollView for setContentView. */
    fun page(root: LinearLayout): View = root.parent as View

    fun title(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 22f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(0, 0, 0, dp(context, 12))
        }

    fun section(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 16f
            setTypeface(typeface, Typeface.BOLD)
            setPadding(0, dp(context, 16), 0, dp(context, 4))
        }

    fun row(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 14f
            setPadding(0, dp(context, 3), 0, dp(context, 3))
        }

    fun mono(context: Context, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 12f
            typeface = Typeface.MONOSPACE
            setPadding(0, dp(context, 2), 0, dp(context, 2))
        }

    fun button(context: Context, text: String, onClick: () -> Unit): Button =
        Button(context).apply {
            this.text = text
            setOnClickListener { onClick() }
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.topMargin = dp(context, 6)
            layoutParams = lp
        }

    fun divider(context: Context): View =
        View(context).apply {
            setBackgroundColor(0xFFCCCCCC.toInt())
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(context, 1)).apply {
                topMargin = dp(context, 8); bottomMargin = dp(context, 8)
            }
        }

    private fun dp(context: Context, v: Int): Int =
        (v * context.resources.displayMetrics.density).toInt()
}
