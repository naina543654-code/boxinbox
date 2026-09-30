package black.android.app.job;

import android.content.Intent;
import top.niunaijun.blackreflection.annotation.BClassNameNotProcess;
import top.niunaijun.blackreflection.annotation.BConstructorNotProcess;

@BClassNameNotProcess("android.app.job.JobWorkItem")
public interface JobWorkItemStatic {
  @BConstructorNotProcess
  JobWorkItem _new(Intent Intent0);
}
