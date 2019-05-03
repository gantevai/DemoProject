package com.example.acer.demoproject.Maps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.example.acer.demoproject.R;

import java.util.List;


public class SearchLocationActivity extends Activity {

    MapView mapView;
    EditText searchEditText;
    Button searchBtn;
    Locator locator;
    GraphicsLayer graphicsLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        mapView = (MapView) findViewById(R.id.map);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchBtn = (Button) findViewById(R.id.searchBtn);


        //Geocoder:
        String url = "http://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer";

        //Locator
        locator = Locator.createOnlineLocator(url);

        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED) {
                    //Search String
                    //String address = searchEditText.getText().toString();
                    Intent intent = getIntent();
                    String address = intent.getStringExtra("address");

                    LocatorFindParameters findParameters = new LocatorFindParameters(address);
                    findParameters.setSourceCountry("Nepal");
                    findParameters.setMaxLocations(3);
                    findParameters.setOutSR(mapView.getSpatialReference());
                    graphicsLayer = new GraphicsLayer();
                    new Geocoder().execute(findParameters);
                }
            }
        });
    }

    public class Geocoder extends AsyncTask<LocatorFindParameters, Void, List<LocatorGeocodeResult>> {
        @Override
        protected List<LocatorGeocodeResult> doInBackground(LocatorFindParameters... params) {
            List<LocatorGeocodeResult> results = null;

            try {
                results = locator.find(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Results of points
            return results;
        }

        @Override
        protected void onPostExecute(List<LocatorGeocodeResult> results) {
            if (results == null || results.size() == 0) {
                Toast.makeText(SearchLocationActivity.this, "No Results Found", Toast.LENGTH_SHORT).show();
            } else {
                LocatorGeocodeResult result = results.get(0);

                Geometry resultLocation = result.getLocation();
                SimpleMarkerSymbol resultSymbol = new SimpleMarkerSymbol(Color.GREEN, 25, SimpleMarkerSymbol.STYLE.TRIANGLE);
                Graphic resultLocationGraphic = new Graphic(resultLocation, resultSymbol);


                graphicsLayer.addGraphic(resultLocationGraphic);
                TextSymbol resultAddress = new TextSymbol(16, result.getAddress(), Color.BLACK);
                resultAddress.setOffsetY(100);

                graphicsLayer.addGraphic(new Graphic(resultLocation, resultAddress));
                mapView.addLayer(graphicsLayer);
                mapView.zoomTo((Point) resultLocation, 70);
            }
        }

    }
}
