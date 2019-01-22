package saeed.example.com.map;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("uRWhPj89wTFvNuFPo1tfCNTe3xQbxcVLFX39ESR1")
                // if defined
                .clientKey("YyRAAW7yvNmNA51YIExQZDdmT2YftVcAhE42gNJs")
                .server("https://parseapi.back4app.com")

                .build()
        );
    }
}
