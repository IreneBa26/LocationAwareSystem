package com.sm.app.utils;

import java.util.HashMap;

/**
 *  Class that provides Constants
 */
public class Constant {
    public static final String FENCE_ENTER_EVENT                                = "ENTER";
    public static final String FENCE_EXIT_EVENT                                 = "EXIT";
    public static final String FENCE_REMAINED_IN_EVENT                          = "REMAINED_IN";
    public static final String FENCE_REMAINED_OUT_EVENT                         = "REMAINED_OUT";
    public static final String FENCE_DISACTIVE                                  = "FENCE_DISACTIVE";

    public static final int DATABASE_VERSION                                    = 1;
    public static final String DATABASE_NAME                                    = "AlertApp.db";

    public static final String TOAST_TEXT_POLLING_SERVICE_START                 = "Polling Strategy è iniziato...";
    public static final String TOAST_TEXT_POLLING_SERVICE_STOP                  = "Polling Strategy si è fermato!";
    public static final String TOAST_TEXT_BETTER_APP_SERVICE_START              = "Better Approach Startegy è iniziato...";
    public static final String TOAST_TEXT_BETTER_APP_SERVICE_STOP               = "Better Approach Strategy si è fermato!";
    public static final String TOAST_TEXT_NO_ADDRESS_FOUND                      = "No address found!";

    public static final String SNACKBAR_TEXT_NEW_FENCE                          = "Aggiungi un Fence!";

    public static final String MENU_TEXT_FENCE_TITLE                            = "Scegli un'azione";
    public static final String MENU_TEXT_FENCE_DELETE                           = "Cancella";
    public static final String MENU_TEXT_FENCE_UPDATE                           = "Aggiorna/Mostra";

    public static final String SEARCH_MY_LOCATION_TEXT                          = "Cercando la mia localizzazione...";

    public static final String TITLE_VIEW_APP                                   = "BS";
    public static final String TITLE_VIEW_HOME                                  = "Home";
    public static final String TITLE_VIEW_MAP_GEOFENCES                         = "Mappa";
    public static final String TITLE_VIEW_LIST_GEOFENCES                        = "Lista Personale di Parcheggi";
    public static final String TITLE_VIEW_START_SERVICE                         = "Avverti Parcheggi Personali";
    public static final String TITLE_VIEW_CREDITS                               = "Informazioni";
    public static final String TITLE_VIEW_UPDATE_FENCE                          = "Aggiorna Fence";
    public static final String TITLE_VIEW_ADD_NEW_FENCE                         = "Aggiungi un nuovo Fence";
    public static final String TITLE_VIEW_INFO                                  = "Informazioni";

    public static final String NOTIFICATION_TEXT                                = "Clicca qua!!";

    public static final long POLLING_UPDATE_REQUEST_MILLIS                      = 5*1000;       // 5 sec
    public static final long UPDATE_REQUEST_MILLIS_5_SEC                        = 5*1000;       // 5 sec
    public static final long UPDATE_REQUEST_MILLIS_30_SEC                       = 30*1000;      // 30 sec
    public static final long UPDATE_REQUEST_MILLIS_1_MIN                        = 60*1000;      // 1 min
    public static final long UPDATE_REQUEST_MILLIS_3_MIN                        = 3*60*1000;    // 3 min
    public static final long UPDATE_REQUEST_MILLIS_5_MIN                        = 5*60*1000;    // 5 min
    public static final long UPDATE_REQUEST_MILLIS_8_MIN                        = 8*60*1000;    // 8 min

    public static final String BUNDLE_FENCE_TO_UPDATE_ID                        = "FENCE_TO_UPDATE";

    public final static int SPINNER_EVENT_ENTER                                 = 0;
    public final static int SPINNER_EVENT_EXIT                                  = 1;
    public final static int SPINNER_EVENT_ENTER_AND_EXIT                        = 2;

    public final static float ALPHA                                             = 0.8f;
    public final static float BETA                                              = 0.1f;
    public final static float GAMMA                                             = 0.1f;

    public final static HashMap SPINNER_RANGE_POSITIONS                         = new HashMap();

    static {
        SPINNER_RANGE_POSITIONS.put(50.0f,    0);
        SPINNER_RANGE_POSITIONS.put(100.0f,   1);
        SPINNER_RANGE_POSITIONS.put(250.0f,   2);
        SPINNER_RANGE_POSITIONS.put(500.0f,   3);
        SPINNER_RANGE_POSITIONS.put(1000.0f,  4);
        SPINNER_RANGE_POSITIONS.put(2000.0f,  5);
        SPINNER_RANGE_POSITIONS.put(5000.0f,  6);
    }
}
