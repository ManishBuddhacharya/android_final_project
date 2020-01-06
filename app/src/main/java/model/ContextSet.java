package model;

import android.content.Context;

public class ContextSet {

        private static Context context;

        public static void setContext(Context cntxt) {
            context = cntxt;
        }

        public static Context getContext() {
            return context;
        }

}
