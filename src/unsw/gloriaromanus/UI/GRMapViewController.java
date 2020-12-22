package unsw.gloriaromanus.UI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.util.Pair;
import unsw.gloriaromanus.ArrayUtil;
import unsw.gloriaromanus.Faction;
import unsw.gloriaromanus.GloriaRomanusGameLogic;
import unsw.gloriaromanus.Province;
import unsw.gloriaromanus.TurnSystem.TurnManager;

public class GRMapViewController {

    // NEW STUFF
    private TurnManager tm = new TurnManager();
    private GloriaRomanusGameLogic grGameLogic;
    private ArrayList<Province> all_provinces = new ArrayList<Province>();

    private String state = "default";

    @FXML
    private MapView mapView;

    @FXML
    private StackPane stackPaneMain;

    @FXML
    private Button mapCenterButton;

    @FXML
    private Label cPlayerText;

    @FXML
    private Label cPlayerGoldText;

    @FXML
    private Label turnText;
    private StringProperty turnText_value;

    // could use ControllerFactory?
    private ArrayList<Pair<GRMapViewMenuController, VBox>> controllerParentPairs;
    private GRMapViewMoveController moveController;
    private GRMapViewInvadeController invadeController;

    private ArcGISMap map;


    private Faction player1;
    private Faction player2;

    private static Color red = Color.rgb(215, 69, 69);
    private static Color blue = Color.rgb(69, 144, 209);

    private boolean selectionAllowed = true;
    private Feature currSelectedProvinceFeature;
    private Feature currSelectedDestinationFeature;
    private Feature currSelectedTargetFeature;
    // private Feature currentlySelectedEnemyProvince;

    private FeatureLayer featureLayer_provinces;

    FeatureCollection fc = null;
    FeatureTable geoPackageTable_provinces;

    

    @FXML
    private void initialize() throws JsonParseException, JsonMappingException, IOException, InterruptedException {

        turnText_value = new SimpleStringProperty("TURN " + tm.getCurrTurn());
        turnText.textProperty().bind(turnText_value);

        // provinceToOwningFactionMap = getProvinceToOwningFactionMap();

        // provinceToNumberTroopsMap = new HashMap<String, Integer>();
        // Random r = new Random();
        // for (String provinceName : provinceToOwningFactionMap.keySet()) {
        // provinceToNumberTroopsMap.put(provinceName, r.nextInt(500));
        // }

        // select in loading screen)
        // humanFaction = "Rome";

        currSelectedProvinceFeature = null;
        // currentlySelectedEnemyProvince = null;

    }

    public void preShow() throws JsonParseException, JsonMappingException, IOException {

        grGameLogic = new GloriaRomanusGameLogic(player1, player2, tm);
        updateTurnText();

        String[] menus = { "GRMapViewDetails.fxml", "GRMapViewManage.fxml", "GRMapViewBuild.fxml", "GRMapViewInvade.fxml", "GRMapViewTest.fxml", "GRMapViewTrain.fxml", "GRMapViewMove.fxml", "VictoryScreen.fxml"};
        controllerParentPairs = new ArrayList<Pair<GRMapViewMenuController, VBox>>();
        for (String fxmlName : menus) {
            System.out.println(fxmlName);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
            VBox root = (VBox) loader.load();
            GRMapViewMenuController menuController = (GRMapViewMenuController) loader.getController();
            menuController.setParent(this);
            menuController.postInit();
            controllerParentPairs.add(new Pair<GRMapViewMenuController, VBox>(menuController, root));
        
            if (fxmlName.equals("GRMapViewMove.fxml")) moveController = (GRMapViewMoveController) menuController;
            if (fxmlName.equals("GRMapViewInvade.fxml")) invadeController = (GRMapViewInvadeController) menuController;
        }

        stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());

        initializeProvinceLayers();

    }

    public void clickedInvadeButton(ActionEvent e) throws IOException {
        // if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince
        // != null){
        // String humanProvince =
        // (String)currentlySelectedHumanProvince.getAttributes().get("name");
        // String enemyProvince =
        // (String)currentlySelectedEnemyProvince.getAttributes().get("name");
        // if (confirmIfProvincesConnected(humanProvince, enemyProvince)){
        // Random r = new Random();
        // int choice = r.nextInt(2);
        // if (choice == 0){
        // // human won. Transfer 40% of troops of human over. No casualties by human,
        // but enemy loses all troops
        // int numTroopsToTransfer = provinceToNumberTroopsMap.get(humanProvince)*2/5;
        // provinceToNumberTroopsMap.put(enemyProvince, numTroopsToTransfer);
        // provinceToNumberTroopsMap.put(humanProvince,
        // provinceToNumberTroopsMap.get(humanProvince)-numTroopsToTransfer);
        // provinceToOwningFactionMap.put(enemyProvince, humanFaction);
        // printMessageToTerminal("Won battle!");
        // }
        // else{
        // // enemy won. Human loses 60% of soldiers in the province
        // int numTroopsLost = provinceToNumberTroopsMap.get(humanProvince)*3/5;
        // provinceToNumberTroopsMap.put(humanProvince,
        // provinceToNumberTroopsMap.get(humanProvince)-numTroopsLost);
        // printMessageToTerminal("Lost battle!");
        // }
        // resetSelections(); // reset selections in UI
        // addAllPointGraphics(); // reset graphics
        // }
        // else{
        // printMessageToTerminal("Provinces not adjacent, cannot invade!");
        // }

        // }
    }

    /**
     * run this initially to update province owner, change feature in each
     * FeatureLayer to be visible/invisible depending on owner. Can also update
     * graphics initially
     */
    private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

        Basemap myBasemap = Basemap.createImagery();
        // myBasemap.getReferenceLayers().remove(0);
        map = new ArcGISMap(myBasemap);
        mapView.setMap(map);

        setColorBasedOnPlayer();

        // note - tried having different FeatureLayers for AI and human provinces to
        // allow different selection colors, but deprecated setSelectionColor method
        // does nothing
        // so forced to only have 1 selection color (unless construct graphics overlays
        // to give color highlighting)
        GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
        gpkg_provinces.loadAsync();
        gpkg_provinces.addDoneLoadingListener(() -> {
            if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
                // create province border feature
                featureLayer_provinces = createFeatureLayer(gpkg_provinces);
                map.getOperationalLayers().add(featureLayer_provinces);

            } else {
                System.out.println("load failure");
            }
        });

        addAllPointGraphics();

        Point mapCenter = new Point(1130000, 5200000, SpatialReferences.getWebMercator());
        mapView.setViewpointCenterAsync(mapCenter, 30000000);
    }

    public void setColorBasedOnPlayer() {

        Faction faction = grGameLogic.getCurrentPlayer();
        int color = (faction.equals(player1)) ? 0xFFD74545 : 0xFF458FD1;

        mapView.getSelectionProperties().setColor(color);

    }

    public void setColorForInvade() {

        int color = 0xFFFFFFFF;
        mapView.getSelectionProperties().setColor(color);

    }

    private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
        mapView.getGraphicsOverlays().clear();

        InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
        fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

        for (org.geojson.Feature f : fc.getFeatures()) {
            if (f.getGeometry() instanceof org.geojson.Point) {

                org.geojson.Point p = (org.geojson.Point) f.getGeometry();
                LngLatAlt coor = p.getCoordinates();
                Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());

                PictureMarkerSymbol s = null;

                String province_name = (String) f.getProperty("name");
                Province province = getProvinceByName(province_name);

                Faction faction = province.getOccupant();
                String faction_name = faction.getName();
                // String faction = provinceToOwningFactionMap.get(province_name);

                int color = (faction.equals(player1)) ? 0xFFD74545 : 0xFF458FD1;

                // TextSymbol t = new TextSymbol(10,
                // faction_name + "\n" + province_name + "\n" +
                // provinceToNumberTroopsMap.get(province_name), color,
                // HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

                TextSymbol t = new TextSymbol(10, faction_name + "\n" + province_name, color,
                        HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

                switch (faction_name) {
                    case "Gaul":
                        // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so
                        // could
                        // construct it with custom-produced BufferedImages stored in Ram
                        // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
                        // then you could convert it to JavaFX image
                        // https://stackoverflow.com/a/30970114

                        // you can pass in a filename to create a PictureMarkerSymbol...
                        s = new PictureMarkerSymbol(
                                new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
                        break;
                    case "Rome":
                        // you can also pass in a javafx Image to create a PictureMarkerSymbol
                        // (different to BufferedImage)
                        s = new PictureMarkerSymbol("images/legionary.png");
                        break;
                }
                t.setHaloColor(0xFFFFFFFF);
                t.setHaloWidth(2);
                Graphic gPic = new Graphic(curPoint, s);
                Graphic gText = new Graphic(curPoint, t);
                graphicsOverlay.getGraphics().add(gPic);
                graphicsOverlay.getGraphics().add(gText);
            } else {
                System.out.println("Non-point geo json object in file");
            }

        }

        inputStream.close();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
    }

    private Province getProvinceByName(String province_name) {

        for (Province p : all_provinces) {
            if (p.getName().equals(province_name))
                return p;
        }

        return null;

    }

    private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
        geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

        // Make sure a feature table was found in the package
        if (geoPackageTable_provinces == null) {
            System.out.println("no geoPackageTable found");
            return null;
        }

        // Create a layer to show the feature table
        FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

        // https://developers.arcgis.com/java/latest/guide/identify-features.htm
        // listen to the mouse clicked event on the map view
        mapView.setOnMouseClicked(e -> {
            // was the main button pressed?
            if (e.getButton() == MouseButton.PRIMARY) {
                // get the screen point where the user clicked or tapped
                Point2D screenPoint = new Point2D(e.getX(), e.getY());

                // specifying the layer to identify, where to identify, tolerance around point,
                // to return pop-ups only, and
                // maximum results
                // note - if select right on border, even with 0 tolerance, can select multiple
                // features - so have to check length of result when handling it
                final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
                        screenPoint, 0, false, 25);

                // add a listener to the future
                identifyFuture.addDoneListener(() -> {
                    try {
                        // get the identify results from the future - returns when the operation is
                        // complete
                        IdentifyLayerResult identifyLayerResult = identifyFuture.get();
                        // a reference to the feature layer can be used, for example, to select
                        // identified features
                        if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
                            FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
                            // select all features that were identified
                            List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f)
                                    .collect(Collectors.toList());

                            if (features.size() == 1) {
                                // note maybe best to track whether selected...
                                Feature f = features.get(0);
                                String province_name = (String) f.getAttributes().get("name");

                                // ZOOM IN TO PROVINCE
                                zoomInToProvince(province_name);

                                // Check current player owns province
                                // Deselect previous province if it exists
                                // Select clicked province and store its feature

                                switch (state){
                                    case "default":
                                        if (currPlayerOwnsProvince(province_name) && selectionAllowed) {

                                            if (currSelectedProvinceFeature != null) {
                                                featureLayer.unselectFeature(currSelectedProvinceFeature);
                                            }
        
                                            currSelectedProvinceFeature = f;
                                            featureLayer.selectFeature(f);
        
                                        }    
                                    break;

                                    case "move":
                                        if (currPlayerOwnsProvince(province_name)) {

                                            String source = (String) currSelectedProvinceFeature.getAttributes().get("name");
                                            if (province_name.equals(source)) break;

                                            if (currSelectedDestinationFeature != null) {
                                                featureLayer.unselectFeature(currSelectedDestinationFeature);
                                            }
        
                                            currSelectedDestinationFeature = f;
                                            featureLayer.selectFeature(f);
                                            
                                            moveController.updateDestination();
                                        }
                                    break;

                                    case "invade":
                                    if (!currPlayerOwnsProvince(province_name)) {

                                        if (currSelectedTargetFeature != null) {
                                            featureLayer.unselectFeature(currSelectedTargetFeature);
                                        }
    
                                        currSelectedTargetFeature = f;
                                        featureLayer.selectFeature(f);
                                        
                                        invadeController.updateTarget();
                                    }
                                    break;

                                }

                                // if (provinceToOwningFactionMap.get(province).equals(humanFaction)){
                                // // province owned by human
                                // if (currentlySelectedHumanProvince != null){
                                // featureLayer.unselectFeature(currentlySelectedHumanProvince);
                                // }
                                // currentlySelectedHumanProvince = f;
                                // if (controllerParentPairs.get(0).getKey() instanceof
                                // GRMapViewInvadeController){
                                // ((GRMapViewInvadeController)controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                                // }

                                // }
                                // else{
                                // if (currentlySelectedEnemyProvince != null){
                                // featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                                // }
                                // currentlySelectedEnemyProvince = f;
                                // if (controllerParentPairs.get(0).getKey() instanceof
                                // GRMapViewInvadeController){
                                // ((GRMapViewInvadeController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                                // }
                                // }

                            }

                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        // ... must deal with checked exceptions thrown from the async identify
                        // operation
                        System.out.println("InterruptedException occurred");
                    }
                });
            }
        });
        return flp;
    }

    private void zoomInToProvince(String province_name) {
        if (fc != null) {
            for (org.geojson.Feature fe : fc.getFeatures()) {
                if (fe.getGeometry() instanceof org.geojson.Point) {

                    String geo_province = (String) fe.getProperty("name");
                    if (geo_province.equals(province_name)) {
                        org.geojson.Point p = (org.geojson.Point) fe.getGeometry();
                        LngLatAlt coor = p.getCoordinates();
                        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(),
                                SpatialReferences.getWgs84());
                        mapView.setViewpointCenterAsync(curPoint, 10000000);
                    }
                }
            }
        }
    }

    private boolean currPlayerOwnsProvince(String province_name) {

        List<Province> p_list = grGameLogic.getCurrentPlayer().getProvince_list();
        for (Province p : p_list) {
            if (p.getName().equals(province_name))
                return true;
        }

        return false;

    }

    // private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    //     String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    //     JSONObject ownership = new JSONObject(content);
    //     Map<String, String> m = new HashMap<String, String>();
    //     for (String key : ownership.keySet()) {
    //         // key will be the faction name
    //         JSONArray ja = ownership.getJSONArray(key);
    //         // value is province name
    //         for (int i = 0; i < ja.length(); i++) {
    //             String value = ja.getString(i);
    //             m.put(value, key);
    //         }
    //     }
    //     return m;
    // }

    private ArrayList<String> getHumanProvincesList(String faction) throws IOException {
        // https://developers.arcgis.com/labs/java/query-a-feature-layer/

        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
        JSONObject ownership = new JSONObject(content);
        return ArrayUtil.convert(ownership.getJSONArray(faction));
    }

    /**
     * returns query for arcgis to get features representing human provinces can
     * apply this to FeatureTable.queryFeaturesAsync() pass string to
     * QueryParameters.setWhereClause() as the query string
     */
    // private String getHumanProvincesQuery() throws IOException {
    // LinkedList<String> l = new LinkedList<String>();
    // for (String hp : getHumanProvincesList()) {
    // l.add("name='" + hp + "'");
    // }
    // return "(" + String.join(" OR ", l) + ")";
    // }

    // private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    //     String content = Files
    //             .readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    //     JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    //     return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
    // }

    // private void resetSelections(){
    // //
    // featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince,
    // currSelectedProvince));
    // // currentlySelectedEnemyProvince = null;
    // currSelectedProvince = null;
    // if (controllerParentPairs.get(0).getKey() instanceof
    // GRMapViewInvadeController){
    // ((GRMapViewInvadeController)controllerParentPairs.get(0).getKey()).setInvadingProvince("");
    // ((GRMapViewInvadeController)controllerParentPairs.get(0).getKey()).setOpponentProvince("");
    // }
    // }

    /**
     * Stops and releases all resources used in application.
     */
    void terminate() {

        if (mapView != null) {
            mapView.dispose();
        }
    }

    // NEW CODE !!!!

    // public void switchMenu() throws JsonParseException, JsonMappingException, IOException {
    //     System.out.println("trying to switch menu");
    //     stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    //     Collections.reverse(controllerParentPairs);
    //     stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
    // }

    @FXML
    void mapCenterButtonPress(ActionEvent event) {
        resetZoom();
    }

    public void createP1Faction(String p1Faction) throws IOException {

        // Get the provinces that belong to this faction.
        ArrayList<String> province_names = getHumanProvincesList(p1Faction);

        ArrayList<Province> province_list = new ArrayList<Province>();

        for (String n : province_names) {

            boolean isLandlocked = checkProvinceLandlocked(n);
            Province temp = new Province(n, isLandlocked, tm);
            province_list.add(temp);
            all_provinces.add(temp);

        }

        player1 = new Faction(p1Faction, province_list);

        for (Province p : province_list) {
            p.setOccupant(player1);
        }
    }

    public void createP2Faction(String p2Faction) throws IOException {

        // Get the provinces that belong to this faction.
        ArrayList<String> province_names = getHumanProvincesList(p2Faction);

        ArrayList<Province> province_list = new ArrayList<Province>();

        for (String n : province_names) {

            boolean isLandlocked = checkProvinceLandlocked(n);
            Province temp = new Province(n, isLandlocked, tm);
            province_list.add(temp);
            all_provinces.add(temp);

        }

        player2 = new Faction(p2Faction, province_list);

        for (Province p : province_list) {
            p.setOccupant(player2);
        }

    }

    private boolean checkProvinceLandlocked(String n) throws IOException {

        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/landlocked_provinces.json"));
        JSONArray landlocked_provinces = new JSONArray(content);
        final ArrayList<String> landlocked = ArrayUtil.convert(landlocked_provinces);

        // System.out.println("Is " + n + " landlocked? "+ landlocked.contains(n));

        return landlocked.contains(n);

    }

    public TurnManager getTm() {
        return tm;
    }

    public Faction getP1() {
        return player1;
    }

    public Faction getP2() {
        return player1;
    }

    public Faction getCP() {
        return grGameLogic.getCurrentPlayer();
    }

    public void updateTurnText() {

        Faction cp = getCP();

        turnText_value.setValue("TURN " + tm.getCurrTurn());

        if (cp.equals(player1)) {
            cPlayerText.setText("PLAYER 1");
            cPlayerGoldText.setText(cp.getFaction_gold() + "G");
            turnText.setTextFill(red);
            cPlayerText.setTextFill(red);
            cPlayerGoldText.setTextFill(red);   
        } else {
            cPlayerText.setText("PLAYER 2");
            cPlayerGoldText.setText(cp.getFaction_gold() + "G");
            turnText.setTextFill(blue);
            cPlayerText.setTextFill(blue);
            cPlayerGoldText.setTextFill(blue);   
        }
        
    }

    public void deselectSelectedProvince() {
        if (currSelectedProvinceFeature != null) {
            featureLayer_provinces.unselectFeature(currSelectedProvinceFeature);
            currSelectedProvinceFeature = null;
        }
    }

    public void deselectDestinationProvince() {
        if (currSelectedDestinationFeature != null) {
            featureLayer_provinces.unselectFeature(currSelectedDestinationFeature);
            currSelectedDestinationFeature = null;
            
        }
    }

    public void deselectTargetProvince() {
        if (currSelectedTargetFeature != null) {
            featureLayer_provinces.unselectFeature(currSelectedTargetFeature);
            currSelectedTargetFeature = null;
        }
    }

    public void changeSelectedProvince(String province_name) {

        if (selectionAllowed) {

            FeatureTable ft = featureLayer_provinces.getFeatureTable();
            
            QueryParameters query = new QueryParameters();
            query.setWhereClause("name LIKE '" + province_name + "'");
            
            ListenableFuture<FeatureQueryResult> tableQueryResult = ft.queryFeaturesAsync(query);
            
            try {
                FeatureQueryResult result = tableQueryResult.get();
                if (result.iterator().hasNext()) {
                    Feature f = result.iterator().next();
                    
                    if (currSelectedProvinceFeature != null) {
                        featureLayer_provinces.unselectFeature(currSelectedProvinceFeature);
                    }
                    currSelectedProvinceFeature = f;
                    featureLayer_provinces.selectFeature(f);
                    
                    zoomInToProvince(province_name);
                    // zoom into feature
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

	public Feature getSelectedProvinceFeature() {
		return currSelectedProvinceFeature;
    }
    
    public Province getSelectedProvince() {
        String province_name = (String) currSelectedProvinceFeature.getAttributes().get("name");
        return getProvinceByName(province_name);
    }

    public Province getDestinationProvince() {
        String province_name = (String) currSelectedDestinationFeature.getAttributes().get("name");
        return getProvinceByName(province_name);
    }

	public void resetZoom() {
        Point mapCenter = new Point(1130000, 5200000, SpatialReferences.getWebMercator());
        mapView.setViewpointCenterAsync(mapCenter, 30000000);
	}

	public void changeToManageMenu() {

        selectionAllowed = false;
        state = "default";

        for (Pair<GRMapViewMenuController, VBox> p : controllerParentPairs ) {
            if (p.getKey() instanceof GRMapViewManageController) {
                GRMapViewManageController mController = (GRMapViewManageController) p.getKey();
                mController.reInit();
                VBox v = p.getValue();
                displayVBox(v);
            }
        }

    }

    public void changeToBuildMenu() {

        selectionAllowed = false;

        for (Pair<GRMapViewMenuController, VBox> p : controllerParentPairs ) {
            if (p.getKey() instanceof GRMapViewBuildController) {
                GRMapViewBuildController bController = (GRMapViewBuildController) p.getKey();
                bController.reInit();
                VBox v = p.getValue();
                displayVBox(v);
            }
        }

    }

    public void changeToTrainMenu() {

        selectionAllowed = false;

        for (Pair<GRMapViewMenuController, VBox> p : controllerParentPairs ) {
            if (p.getKey() instanceof GRMapViewTrainController) {
                GRMapViewTrainController tController = (GRMapViewTrainController) p.getKey();
                tController.reInit();
                VBox v = p.getValue();
                displayVBox(v);
            }
        }

    }

    public void changeToMoveMenu() {

        state = "move";

        for (Pair<GRMapViewMenuController, VBox> p : controllerParentPairs ) {
            if (p.getKey() instanceof GRMapViewMoveController) {
                GRMapViewMoveController mController = (GRMapViewMoveController) p.getKey();
                mController.reInit();
                VBox v = p.getValue();
                displayVBox(v);
            }
        }

    }

    public void changeToInvadeMenu() {

        state = "invade";
        setColorForInvade();

        for (Pair<GRMapViewMenuController, VBox> p : controllerParentPairs ) {
            if (p.getKey() instanceof GRMapViewInvadeController) {
                GRMapViewInvadeController iController = (GRMapViewInvadeController) p.getKey();
                iController.reInit();
                VBox v = p.getValue();
                displayVBox(v);
            }
        }

    }

    public void changeToVictoryMenu() {

        selectionAllowed = false;
        setColorForInvade();

        for (Pair<GRMapViewMenuController, VBox> p : controllerParentPairs ) {
            if (p.getKey() instanceof VictoryScreenController) {
                VictoryScreenController vController = (VictoryScreenController) p.getKey();
                vController.reInit();
                VBox v = p.getValue();
                displayVBox(v);
            }
        }

    }

    
    public void changeToDetailsMenu() {

        selectionAllowed = true;
        state = "default";

        for (Pair<GRMapViewMenuController, VBox> p : controllerParentPairs ) {
            if (p.getKey() instanceof GRMapViewDetailsController) {
                GRMapViewDetailsController dController = (GRMapViewDetailsController) p.getKey();
                dController.reInit();
                VBox v = p.getValue();
                displayVBox(v);
            }
        }

        deselectSelectedProvince();
        resetZoom();

	}

    public void displayVBox(VBox v) {

        for (Node n : stackPaneMain.getChildren()) {
            if (n instanceof VBox) {
                stackPaneMain.getChildren().remove(n);
                break;
            }
        }
            
        stackPaneMain.getChildren().add(v);

    }

	public Province getTargetProvince() {
        String province_name = (String) currSelectedTargetFeature.getAttributes().get("name");
        return getProvinceByName(province_name);
	}

    public GloriaRomanusGameLogic getGrGameLogic() {
        return grGameLogic;
    }

    public void setGrGameLogic(GloriaRomanusGameLogic grGameLogic) {
        this.grGameLogic = grGameLogic;
    }


}
