/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.DashBoard;
import vn.mobifone.loaphuong.entity.MCU;
import vn.mobifone.loaphuong.entity.TimeTable;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.webservice.AreaWebservice;
import vn.mobifone.loaphuong.webservice.CallWebservice;
import vn.mobifone.loaphuong.webservice.LoginWebservice;

/**
 *
 * @author ChienDX
 */
@ManagedBean(name = "DashBoardController")
@ViewScoped
public class DashBoardController implements Serializable {

    
    private DashBoard dbData;
    
    private MapModel advancedModel;
    private AreaTree areaRoot;
    private DocumentService service;
    private Marker marker;
    private AreaTree selectedArea;
    private List<MCU> listMCU;
    private AreaWebservice coreWS;
    private LatLng center;
    private int status;
    private boolean state;
    private boolean allowHouseManagement = true;
    private boolean isCityMode = true;

    
    public DashBoardController() {
        
        dbData = (new LoginWebservice()).getListDashBoardByArea(SecUser.getUserLogged().getAreaId());
        center = new LatLng(Double.parseDouble(SystemConfig.getConfig("lat")), Double.parseDouble(SystemConfig.getConfig("long")));
        //center = new LatLng(17.020373,107.022327);
         //Area tree
        service = new DocumentService();
        areaRoot = service.createAreaTree();
        areaRoot.getChildren().get(0).setSelected(true);
        areaRoot.getChildren().get(0).setExpanded(true);
        selectedArea = (AreaTree) areaRoot.getChildren().get(0);
        
        if (Session.getSessionValue(SessionKeyDefine.TIME_TABLE_LIST) == null) {
            List<TimeTable> listTimeTable = new LoginWebservice().getTimeTableByArea(Long.parseLong(SystemConfig.getConfig("domainArea")), ((AreaTree) areaRoot.getChildren().get(0)).getAreaType());
            Session.setSessionValue(SessionKeyDefine.TIME_TABLE_LIST, listTimeTable);
            
            String timeTableString = "";
            for (TimeTable timeTable : listTimeTable) {
                timeTableString += convertNumberToTime(timeTable.getStart_time()) + " - " + convertNumberToTime(timeTable.getEnd_time()) + " , ";
            }
            Session.setSessionValue(SessionKeyDefine.TIME_TABLE_STRING, timeTableString);
        }
        
        
        //allowHouseManagement = Integer.parseInt(SystemConfig.getConfig("viewHouseManagement"))==1?true:false;
        allowHouseManagement = (boolean)Session.getSessionValue(SessionKeyDefine.ALLOW_HOUSE_MANAGEMENT);
        isCityMode = Integer.parseInt(SystemConfig.getConfig("areaMode"))==2?true:false;
        coreWS = new AreaWebservice();
        status = 0; 
        state = false;
        loadMCU(0);
        
    }   

    public DashBoard getDbData() {
        return dbData;
    }

    public void setDbData(DashBoard dbData) {
        this.dbData = dbData;
    }
    
    private void loadMCU(int mode) {    // 0: all / 1:connecting / 2:disconected
        
        advancedModel = new DefaultMapModel();
        listMCU = coreWS.getListMCUByArea(selectedArea.getAreaId());
          
        //Shared coordinates
        
        if (listMCU.size() > 0) {
            double maxLat = listMCU.get(0).getMcu_lat();
            double minLat = listMCU.get(0).getMcu_lat();
            double maxLong = listMCU.get(0).getMcu_lng();
            double minLong = listMCU.get(0).getMcu_lng();
            
            for (MCU mcu : listMCU) {
                if (mcu.getMcu_lat() > maxLat) {
                    maxLat = mcu.getMcu_lat();
                } else if (mcu.getMcu_lat() < minLat) {
                    minLat = mcu.getMcu_lat();
                }

                if (mcu.getMcu_lng() > maxLong) {
                    maxLong = mcu.getMcu_lng();
                } else if (mcu.getMcu_lng() < minLong) {
                    minLong = mcu.getMcu_lng();
                }

                String t  = Session.getContextPath(FacesContext.getCurrentInstance()) + "/resources/images/green-dot1.png";

                if (mcu.getMcu_connect_status() == 1 && mode != 2) {  // đang kết nôi
                    advancedModel.addOverlay(new Marker(new LatLng(mcu.getMcu_lat(), mcu.getMcu_lng()), mcu.getMcu_type()==1?mcu.getHolder_name():mcu.getMira_name(), mcu, "http://maps.google.com/mapfiles/ms/micons/green-dot.png", "Đang kết nối"));

                } 
                
                if (mcu.getMcu_connect_status() == 0 && mode != 1) {
                    String operator = SystemConfig.getConfig("operator");
                    if ((operator != null && operator.length()>0) || SecUser.isSuperAdmin()){
                        advancedModel.addOverlay(new Marker(new LatLng(mcu.getMcu_lat(), mcu.getMcu_lng()), mcu.getMcu_type()==1?mcu.getHolder_name():mcu.getMira_name(), mcu, "http://maps.google.com/mapfiles/ms/micons/red-dot.png", "Mất kết nối"));// mất kết nối
                    }else{
                        advancedModel.addOverlay(new Marker(new LatLng(mcu.getMcu_lat(), mcu.getMcu_lng()), mcu.getMcu_type()==1?mcu.getHolder_name():mcu.getMira_name(), mcu, "http://maps.google.com/mapfiles/ms/micons/green-dot.png", "Đang kết nối"));
                    }
                }
            }
            
            center = new LatLng((minLat + maxLat)/2, (minLong + maxLong)/2);
        } else {
            //center = new LatLng(17.020373,107.022327);
            //center = new LatLng(21.0227387,105.8194541);
            center = new LatLng(Double.parseDouble(SystemConfig.getConfig("lat")), Double.parseDouble(SystemConfig.getConfig("long")));
        }
        

    }
    
    private static String convertNumberToTime(long num){
        String time = num/60 + ":" + (num%60 < 10 ? "0" + num%60 : num%60);
        return time;
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
        if (state) {
            loadMCU(status);
        }
        state = true;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }
    
    
    public void changeStatusEvent(ActionEvent evt) { 
        
    } 

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        loadMCU(status);
    }

    public boolean isAllowHouseManagement() {
        return allowHouseManagement;
    }

    public boolean isIsCityMode() {
        return isCityMode;
    }


    
}
