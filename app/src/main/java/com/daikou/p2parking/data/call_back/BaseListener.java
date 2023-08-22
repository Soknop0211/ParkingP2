package com.daikou.p2parking.data.call_back;

import java.util.HashMap;

public interface BaseListener {
    void onError(String message, Integer code, HashMap<String, String> errorKey);
}
