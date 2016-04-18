package drawable.listener;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by tjnorris on 9/29/15.
 */
public class StandardJsonErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {

        System.out.println("NOOOOOO!!! (Darth Vader Voice)");
        error.printStackTrace();
    }
}
