/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

/**
 *
 * @author cuong.trinh
 */
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
  
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.MCU;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.webservice.AreaWebservice;
import vn.mobifone.loaphuong.webservice.CallWebservice;
  
@ManagedBean(name = "MCUMapController")
@ViewScoped
public class MCUMapController implements Serializable {
      
    private MapModel advancedModel;
    private AreaTree areaRoot;
    private DocumentService service;
    private Marker marker;
    private AreaTree selectedArea;
    private List<MCU> listMCU;
    private AreaWebservice coreWS;
    private LatLngBounds bounds;
  

    
    @PostConstruct
    public void init() {
        
        //Area tree
        service = new DocumentService();
        areaRoot = service.createAreaTree();
        areaRoot.getChildren().get(0).setSelected(true);
        areaRoot.getChildren().get(0).setExpanded(true);
        selectedArea = (AreaTree) areaRoot.getChildren().get(0);
        
        
        
        coreWS = new AreaWebservice();
        
        loadMCU();

        
    }
    
    private void loadMCU() {
        listMCU = coreWS.getListMCUByArea(selectedArea.getAreaId());
          
        //Shared coordinates

        
//        double maxLat = listMCU.get(0).getMcu_lat();
//        double minLat = listMCU.get(0).getMcu_lat();
//        double maxLong = listMCU.get(0).getMcu_lng();
//        double minLong = listMCU.get(0).getMcu_lng();
       
        advancedModel = new DefaultMapModel();
        for (MCU mcu : listMCU) {
            
//            if (mcu.getMcu_lat() > maxLat) {
//                maxLat = mcu.getMcu_lat();
//            } else if (mcu.getMcu_lat() < minLat) {
//                minLat = mcu.getMcu_lat();
//            }
//            
//            if (mcu.getMcu_lng() > maxLong) {
//                maxLong = mcu.getMcu_lng();
//            } else if (mcu.getMcu_lng() < minLong) {
//                minLong = mcu.getMcu_lng();
//            }
            
            if (mcu.getMcu_connect_status() == 1) {  // đang kết nôi
                advancedModel.addOverlay(new Marker(new LatLng(mcu.getMcu_lat(), mcu.getMcu_lng()), mcu.getHolder_name(), mcu.getHousehold_address(), "http://maps.google.com/mapfiles/ms/micons/green-dot.png", "Đang kết nối"));
                
            } else { 
                String operator = SystemConfig.getConfig("operator");
                if ((operator != null && operator.length()>0)|| SecUser.isSuperAdmin()) {
                    advancedModel.addOverlay(new Marker(new LatLng(mcu.getMcu_lat(), mcu.getMcu_lng()), mcu.getHolder_name(), mcu.getHousehold_address(), "http://maps.google.com/mapfiles/ms/micons/red-dot.png", "Mất kết nối"));// mất kết nối
                }else{
                    advancedModel.addOverlay(new Marker(new LatLng(mcu.getMcu_lat(), mcu.getMcu_lng()), mcu.getHolder_name(), mcu.getHousehold_address(), "http://maps.google.com/mapfiles/ms/micons/green-dot.png", "Đang kết nối"));
                }
            }
            
            //bounds = new LatLngBounds(new LatLng(maxLat, maxLong), new LatLng(minLat, minLong));
            

        }
 
        
    }
  
    public MapModel getAdvancedModel() {
        return advancedModel;
    }
      
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }
      
    public Marker getMarker() {
        return marker;
    }

    public AreaTree getAreaRoot() {
        return areaRoot;
    }

    public void setAreaRoot(AreaTree areaRoot) {
        this.areaRoot = areaRoot;
    }

    public AreaTree getSelectedArea() {
        return selectedArea;
        
    }

    public void setSelectedArea(AreaTree selectedArea) {
        this.selectedArea = selectedArea;
        loadMCU();
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }
    
    
    
    
}