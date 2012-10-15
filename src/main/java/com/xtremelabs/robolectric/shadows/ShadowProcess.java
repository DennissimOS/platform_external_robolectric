package com.xtremelabs.robolectric.shadows;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

import java.lang.management.ManagementFactory;

@Implements(android.os.Process.class)
public class ShadowProcess {

    private static Integer pid;

    @Implementation
    public static final int myPid() {
        if (pid != null) {
           return pid;
        }

        // Returns machine info in the form {pid}@{ComputerName}
        String  machineId = ManagementFactory.getRuntimeMXBean().getName();

        String[] parts = machineId.split("@");
        if (parts.length == 2) {
            try {
                return Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                // Shouldn't occur on most systems, but default to zero in case it does.
            }
        }
        return 0;
    }

    public static void setPid(int pid) {
        ShadowProcess.pid = pid;
    }

    public static void reset() {
        ShadowProcess.pid = null;
    }
}
