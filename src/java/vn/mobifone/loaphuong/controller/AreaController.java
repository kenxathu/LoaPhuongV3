/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import be.tarsos.transcoder.Attributes;
import be.tarsos.transcoder.Transcoder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import vn.mobifone.loaphuong.entity.AiCamera;
import vn.mobifone.loaphuong.entity.AiCameraMCU;
import vn.mobifone.loaphuong.entity.AiCameraSlot;
import vn.mobifone.loaphuong.model.AreaModel;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.DataResponse;
import vn.mobifone.loaphuong.entity.HouseHold;
import vn.mobifone.loaphuong.entity.MCU;
import vn.mobifone.loaphuong.entity.MCULog;
import vn.mobifone.loaphuong.entity.MCUPlayStateList;
import vn.mobifone.loaphuong.entity.MCURecord;
import vn.mobifone.loaphuong.entity.MCUScheduleList;
import vn.mobifone.loaphuong.entity.MCUState;
import vn.mobifone.loaphuong.entity.MCUStatusLog;
import vn.mobifone.loaphuong.entity.RadioChannel;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.Session;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.TSPermission;
import vn.mobifone.loaphuong.lib.config.SessionKeyDefine;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.util.DateUtil;
import vn.mobifone.loaphuong.util.ResourceBundleUtil;
import vn.mobifone.loaphuong.webservice.AreaWebservice;

/**
 *
 * @author cuong.trinh
 */
@ManagedBean(name = "AreaController")
@ViewScoped
public class AreaController extends TSPermission implements Serializable {

    private TreeNode selectedNode;
    private AreaTree selectedArea;
    private AreaTree areaRoot;
    private AreaTree addedArea;
    private List<HouseHold> listHouse;
    private List<MCU> listMCU;
    private List<MCU> listHouseMCU;
    private List<MCU> listMCUAdded;
    private int tabSelect;
    private AreaModel areaModel;
    private List<Long> treeExpandedNode;
    
    private boolean allowHouseManagement;
    private boolean isCityMode;
    
    // flag for ADD/EDIT/DELETE...
    private PersistAction acttionFlag;
    private PersistAction houseActtionFlag;
    private PersistAction mcuActtionFlag;
    private HouseHold selectedHouse;
    private MCU selectedMCU;
    private AreaWebservice areaWS;
    
    
    // Radio
    private List<RadioChannel> listChannelByArea;
    private List<RadioChannel> listAllChannel;
    private List<Long> listAreaChannelId; 
    private long selectedChannelId;

    private DocumentService service;
    
    private List<MCURecord> listMCURecord;
    // Operation Flag -- Enum
    

    
        // Camera
    private List<AiCamera> listAiCameraByArea;
    private AiCamera selectedAiCamera;
    private PersistAction cameraActtionFlag;

    // AI Camera Mcu
    private List<MCUForAICameraDirection> listMCUForAICameraDirections;
    


    // Ai CAmera Slot
    private String startTimeAdded;
    private String endTimeAdded;
    private String notifyTimeLive;
    private String notifyTimeOff;
    private int typeSlot;
    private List<SlotForAiCamera> listSlotForAiCamerasLive;
    private List<SlotForAiCamera> listSlotForAiCamerasOff;
    private List<SlotForAiCamera> listSlotForAiCamerasAll;

    public static enum PersistAction {

        SELECT,
        CREATE,
        CREATE2,
        DELETE,
        VIEW,
        UPDATE
    }

    //@ManagedProperty("#{documentService}")
    public AreaController() {

        service = new DocumentService();
        areaRoot = service.createAreaTree();
        areaRoot.getChildren().get(0).setSelected(true);
        areaRoot.getChildren().get(0).setExpanded(true);
        selectedArea = (AreaTree) areaRoot.getChildren().get(0);
        treeExpandedNode = new ArrayList<>();
        
        //allowHouseManagement = Integer.parseInt(SystemConfig.getConfig("viewHouseManagement"))==1?true:false;
        allowHouseManagement = (boolean)Session.getSessionValue(SessionKeyDefine.ALLOW_HOUSE_MANAGEMENT);
        isCityMode = Integer.parseInt(SystemConfig.getConfig("areaMode"))==2?true:false;

        this.selectedArea = new AreaTree();
        areaModel = new AreaModel();
        listHouse = new ArrayList<>();
        listChannelByArea = new ArrayList<>();
        listHouseMCU = new ArrayList<>();
        acttionFlag = PersistAction.SELECT;
        houseActtionFlag = PersistAction.SELECT;
        mcuActtionFlag = PersistAction.SELECT;
        areaWS = new AreaWebservice();

        if (ResourceBundleUtil.getCurrentPath().equalsIgnoreCase("area-management")) {
            tabSelect = 0;
        } else if (ResourceBundleUtil.getCurrentPath().equalsIgnoreCase("mcu-management") && allowHouseManagement) {
            tabSelect = 2;
        } else if (ResourceBundleUtil.getCurrentPath().equalsIgnoreCase("camera-management") && allowHouseManagement) {
            tabSelect = 4;
        } else {
            tabSelect = 1;
        }
        
        Calendar cal = Calendar.getInstance();  
        // date for timeline
        toDateForTimeline = new Date();
        fromDateForLog = toDateForTimeline;
        cal.add(Calendar.DATE, -7);  
        fromDateForTimeline = cal.getTime();

        //areaWS.getMCUStateTimeline();
        //System.out.println(ResourceBundleUtil.getCurrentPath());
        listAiCameraByArea = new ArrayList<>();

        listMCUForAICameraDirections = new ArrayList<>();
        listMCUForAICameraDirections.add(new MCUForAICameraDirection(0));
        listMCUForAICameraDirections.add(new MCUForAICameraDirection(1));
        listMCUForAICameraDirections.add(new MCUForAICameraDirection(2));
        listMCUForAICameraDirections.add(new MCUForAICameraDirection(3));

    }

    public int getTabSelect() {
        return tabSelect;
    }

    public void setTabSelect(int tabSelect) {
        this.tabSelect = tabSelect;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;

        // System.out.println("vn.mobifone.controller.AreaController.displaySelectedSingle()" + "select note " + selectedArea.getAreaName());
    }

    public void setService(DocumentService service) {
        this.service = service;
    }

    public void displaySelectedSingle() {

    }
    
    public void resetTree(TreeNode node) {
        List<TreeNode> child = node.getChildren();
        node.setSelected(false);
        node.setExpanded(false);
        
        if (selectedArea.getAreaId() - ((AreaTree)node).getAreaId() == 0) {
            //setSelectedArea((AreaTree)node);
            node.setSelected(true);
        }
        
        for (Long areaId : treeExpandedNode) {
            if (areaId - ((AreaTree)node).getAreaId() == 0) {
                node.setExpanded(true);
                break;
            }
        }
        
        
        for (TreeNode treeNode : child) {
            resetTree(treeNode);
        }
        
    }
    
    public void editHeaderEvent() {
        type = 1;
        receiveId  = selectedArea.getAreaId();
        audioFile = null;
        audioPath = "";
        fileName = "";
    }
    
    public void editAreaEvent() {
        addedArea = selectedArea;
        acttionFlag = PersistAction.UPDATE;
    }

    public void deleteAreaEvent() {
        addedArea = selectedArea;
        acttionFlag = PersistAction.DELETE;
    }

    public void addAreaEvent() {

        acttionFlag = PersistAction.CREATE;

        //System.out.println("vn.mobifone.controller.AreaController.displaySelectedSingle()" + "select into  note " + selectedArea.getAreaName());
        addedArea = new AreaTree();
        addedArea.setParentAreaId(selectedArea.getAreaId());
        addedArea.setAreaType(selectedArea.getAreaType() + 1);
        audioFile = null;
    }

    public void saveHeader() {
        try {
            
            uploadRecordToFileServer(audioFile);
            DataResponse response;
            response = areaWS.addHeader(type, receiveId, audioPath);

            
        } catch (Exception ex) {
            Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
//    public void saveMCUHeader() {
//        try {
//            
//            uploadRecordToFileServer(audioFile);
//            DataResponse response;
//            response = areaWS.addHeader(2, selectedMCU.getMcu_id(), audioPath);
//
//            
//        } catch (Exception ex) {
//            Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//    
     public void saveArea() {
        try {
            DataResponse response;

            if (acttionFlag == PersistAction.CREATE) {
                response = areaWS.addArea(addedArea);
                
                
                
                if (response.getCode() == 200) {
                    ClientMessage.logSuccess("Thêm địa bàn thành công");
                } else {
                    ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
                }


            } else if (acttionFlag == PersistAction.UPDATE) {
                response = areaWS.updateArea(addedArea);
                if (response.getCode() == 200) {
                    
//                    if (!uploadRecordToFileServer(audioFile)) {
//                           ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Không thành công: Đã xảy ra lỗi trong quá trình upload file!");
//                           return;
//                    }
//                    response = areaWS.updateHeader(9, addedArea.getAreaId(), 1, addedArea.getAudio_path());
//                    if (response.getCode() == 200) {
//                        ClientMessage.logSuccess("Cập nhật địa bàn thành công");
//                    } else {
//                        ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
//                    }
                    
                    ClientMessage.logSuccess("Cập nhật địa bàn thành công");
                } else {
                    ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
                }

            } else if (acttionFlag == PersistAction.DELETE) {
                if (selectedArea.getChildren().size() > 0 || listHouse.size() > 0 || listMCU.size()>0) {
                    ClientMessage.logErr("Địa bàn gồm nhiều địa bàn trực thuộc hoặc có hộ gia đình hoặc có thiết bị. Không được xóa!" );
                    return;
                }
                
                response = areaWS.deleteArea(addedArea.getAreaId());
                if (response.getCode() == 200) {
                    ClientMessage.logSuccess("Xóa địa bàn thành công");
                    selectedArea = (AreaTree)selectedArea.getParent();
                } else {
                    ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
                }
            }
            areaRoot = service.createAreaTree();
            resetTree(areaRoot);
            areaRoot.getChildren().get(0).setExpanded(true);
        } catch (Exception ex) {
            Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addHouseEvent() {

        houseActtionFlag = PersistAction.CREATE;

        //System.out.println("vn.mobifone.controller.AreaController.displaySelectedSingle()" + " add house ");
        selectedHouse = new HouseHold();
        selectedHouse.setArea_id(selectedArea.getAreaId());
        selectedHouse.setArea_name(selectedArea.getAreaName());
        listMCUAdded = new ArrayList<>();
        listHouseMCU = new ArrayList<>();
        selectedMCU = new MCU();

    }

    public void viewHouseEvent(HouseHold house) {

        houseActtionFlag = PersistAction.VIEW;
        selectedHouse = house;
        selectedHouse.setAreaName(selectedArea.getAreaName());
        listHouseMCU = areaWS.getListMCUByHouse(house.getId());

    }

    public void editHouseEvent(HouseHold house) {
        houseActtionFlag = PersistAction.UPDATE;
        selectedHouse = house;
        selectedHouse.setAreaName(selectedArea.getAreaName());
        listHouseMCU = areaWS.getListMCUByHouse(house.getId());
        selectedMCU = new MCU();
    }

    public void deleteHouseEvent(HouseHold house) {
        houseActtionFlag = PersistAction.DELETE;
        selectedHouse = house;
    }
    
   
    
    public void deleteMCU(ActionEvent evt) {
        DataResponse response = areaWS.deleteMCU(selectedMCU.getMcu_id());
        if (response.getCode() == 200) {
            ClientMessage.logSuccess( "Xóa mGateway thành công!");
            listMCU = areaWS.getListMCUByArea(selectedArea.getAreaId());
            listHouseMCU = areaWS.getListMCUByHouse(selectedHouse.getId());
        } else {
            ClientMessage.logErr("Không thành công: " + response.getCodeDescVn());
        }
    }
    
    public void rebootMCU(ActionEvent evt) {
        areaWS.rebootMCU(selectedMCU.getMcu_id());
        
    }
    
    public void rebootMCUEvent(MCU mcu) {
        selectedMCU = mcu;
    }
    public void syncMCU(ActionEvent evt) {
        areaWS.syncMCU(selectedMCU.getMcu_id());
        
    }
    
    public void syncMCUEvent(MCU mcu) {
        selectedMCU = mcu;
    }

    public void saveHouse(ActionEvent evt) {
        try {
            DataResponse response = new DataResponse();
            if (houseActtionFlag == PersistAction.CREATE) {
                response = areaWS.addHouseHold(selectedHouse);

            } else if (houseActtionFlag == PersistAction.UPDATE) {
                response = areaWS.updateHouseHold(selectedHouse);
            } else if (houseActtionFlag == PersistAction.DELETE) {
                response = areaWS.deleteHouseHold(selectedHouse.getId());
            }

            if (response.getCode() == 200) {    // thanh cong
                // load lại anh sách hộ gd
                listHouse = areaWS.getListHouseByArea(selectedArea.getAreaId());
            }

        } catch (Exception ex) {
            Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addMCUEvent() {
        selectedMCU = new MCU();
        selectedMCU.setHolder_name(selectedHouse.getHolder_name());
        selectedMCU.setHousehold_id(selectedHouse.getId());
        selectedMCU.setMcu_area_id(selectedHouse.getArea_id());

    }
    
    public void addMCUEvent(HouseHold house) { 
       mcuActtionFlag = PersistAction.CREATE;
       selectedMCU = new MCU();
       
       selectedMCU.setHolder_name(house.getHolder_name());
       selectedMCU.setHousehold_id(house.getId());
       //selectedMCU.setMcu_area_id(house.getArea_id());
    } 
    
     public void viewMCUEvent(MCU mcu) {
        mcuActtionFlag = PersistAction.VIEW;
        selectedMCU = mcu;
        selectedMCU.setPhone_number(mcu.getMcu_tel());
    }
    
    public void updateMCUEvent(MCU mcu) {
        mcuActtionFlag = PersistAction.UPDATE;
        selectedMCU = new MCU(mcu);
    }
    
    public void updateMCUHeaderEvent(MCU mcu) {
        type = 2; 
        receiveId = mcu.getMcu_id();
        fileName = "";
        audioFile = null;
    }
    
    public void deleteMCUEvent(MCU mcu) {
        mcuActtionFlag = PersistAction.DELETE;
        selectedMCU = mcu;
    }
    
    public void addPublicMCUEvent(ActionEvent evt) { 
       selectedMCU = new MCU();
       mcuActtionFlag = PersistAction.CREATE2;
       selectedMCU.setMcu_area_id(selectedArea.getAreaId());
    }  
    
    public void addChannelForArea(ActionEvent evt) { 
       listAllChannel = new ArrayList<>();
       listAllChannel = areaWS.getListChannel();
       //listAreaChannelId = new long[listChannelByArea.size()];
       listAreaChannelId = new ArrayList<>();
        for (int i = 0; i < listChannelByArea.size(); i++) {
            listAreaChannelId.add(listChannelByArea.get(i).getId());
        }
    } 

    public void updateRadioArea(ActionEvent evt) { 
       areaWS.updateRadioArea(selectedArea.getAreaId(), listAreaChannelId);
       listChannelByArea = areaWS.getListChannelByArea(selectedArea.getAreaId(), 0);
    }

    public void saveMCU(ActionEvent evt) {
        try {
            
            if (selectedMCU.getMcu_lat() < 7 || selectedMCU.getMcu_lat() > 23.2) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR,"Vĩ độ không nằm trong địa phận Việt Nam! Mời nhập vĩ độ trong khoảng từ 7.0 - 23.2");
                return;
            }
            if (selectedMCU.getMcu_lng() < 100 || selectedMCU.getMcu_lng() > 116) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR,"Kinh độ không nằm trong địa phận Việt Nam! Mời nhập kinh độ trong khoảng từ 100 - 116");
                return;
            }
            
            
            DataResponse response = null;
            if (mcuActtionFlag == PersistAction.CREATE) {
                response = areaWS.addMCU(selectedMCU);
            } else if (mcuActtionFlag == PersistAction.CREATE2) {
                response = areaWS.addPublicMCU(selectedMCU);
            } else if (mcuActtionFlag == PersistAction.UPDATE) {
                response = areaWS.updateMCU(selectedMCU);
            }


            if (response.getCode() == 200) {
                ClientMessage.logSuccess(mcuActtionFlag == PersistAction.UPDATE? "Cập nhật thông tin thành công" : "Thêm thiết bị thành công!");
                listMCU = areaWS.getListMCUByArea(selectedArea.getAreaId());
            } else {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR,"Không thành công: " + response.getCodeDescVn());
                return;
            }

//            } else if (houseActtionFlag == PersistAction.CREATE) {
//                listHouseMCU.add(selectedMCU);
//                listMCUAdded.add(selectedMCU);
//            }

        } catch (Exception ex) {
            Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteNode() {
        System.out.println("vn.mobifone.controller.AreaController.displaySelectedSingle()" + " delete note");
        selectedNode.getChildren().clear();
        selectedNode.getParent().getChildren().remove(selectedNode);
        selectedNode.setParent(null);

        selectedNode = null;
    }

    public AreaTree getSelectedArea() {
        return selectedArea;
    }

    public void setSelectedArea(AreaTree selectedArea) {
        this.selectedArea = selectedArea;

        try {
            if (selectedArea != null) {
               // System.out.println("vn.mobifone.controller.AreaController.displaySelectedSingle()" + "select area " + selectedArea.getAreaName());

                if (allowHouseManagement) {
                    listHouse = areaWS.getListHouseByArea(selectedArea.getAreaId());
                }
                
                listChannelByArea = areaWS.getListChannelByArea(selectedArea.getAreaId(), 0);
                
                listMCU = areaWS.getListMCUByArea(selectedArea.getAreaId());

                listAiCameraByArea = areaWS.getAiCameraListByArea(selectedArea.getAreaId());

            }
        } catch (Exception ex) {
            Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void onNodeExpand(NodeExpandEvent event) {
        treeExpandedNode.add(((AreaTree)event.getTreeNode()).getAreaId());
        
    }
 
    public void onNodeCollapse(NodeCollapseEvent event) {
        treeExpandedNode.remove(((AreaTree)event.getTreeNode()).getAreaId());        
    }

    public AreaTree getAreaRoot() {
        return areaRoot;
    }

    public void setAreaRoot(AreaTree areaRoot) {
        this.areaRoot = areaRoot;
    }

    public AreaTree getAddedArea() {
        return addedArea;
    }

    public void setAddedArea(AreaTree addedArea) {
        this.addedArea = addedArea;
    }

    public List<HouseHold> getListHouse() {
        return listHouse;
    }

    public void setListHouse(List<HouseHold> listHouse) {
        this.listHouse = listHouse;
    }

    public PersistAction getHouseActtionFlag() {
        return houseActtionFlag;
    }

    public void setHouseActtionFlag(PersistAction houseActtionFlag) {
        this.houseActtionFlag = houseActtionFlag;
    }

    public HouseHold getSelectedHouse() {
        return selectedHouse;
    }

    public void setSelectedHouse(HouseHold selectedHouse) {
        this.selectedHouse = selectedHouse;
    }

    public List<MCU> getListHouseMCU() {
        return listHouseMCU;
    }

    public void setListHouseMCU(List<MCU> listMCU) {
        this.listHouseMCU = listMCU;
    }

    public MCU getSelectedMCU() {
        return selectedMCU;
    }

    public void setSelectedMCU(MCU selectedMCU) {
        this.selectedMCU = selectedMCU;
    }

    public List<MCU> getListMCU() {
        return listMCU;
    }

    public void setListMCU(List<MCU> listMCU) {
        this.listMCU = listMCU;
    }

    public PersistAction getActtionFlag() {
        return acttionFlag;
    }

    public void setActtionFlag(PersistAction acttionFlag) {
        this.acttionFlag = acttionFlag;
    }

    public PersistAction getMcuActtionFlag() {
        return mcuActtionFlag;
    }

    public void setMcuActtionFlag(PersistAction mcuActtionFlag) {
        this.mcuActtionFlag = mcuActtionFlag;
    }

    public List<RadioChannel> getListChannel() {
        return listChannelByArea;
    }

    public void setListChannel(List<RadioChannel> listChannel) {
        this.listChannelByArea = listChannel;
    }
    
    //- ----  quyen mcu
    public boolean isAllowUpdateMCU(){
        return super.isIsAllowUpdate("EDIT_MCU") ;
    }
    public boolean isAllowInsertMCU(){
        return super.isIsAllowInsert("ADD_MCU") ;
    }
    public boolean isAllowDeleteMCU(){
        return super.isIsAllowDelete("DELETE_MCU") ;
    }
    
     //- ----  quyen dia ban
    public boolean isAllowUpdateAREA(){
        return super.isIsAllowUpdate("EDIT_AREA") ;
    }
    public boolean isAllowInsertAREA(){
        return super.isIsAllowInsert("ADD_AREA") ;
    }
    public boolean isAllowDeleteAREA(){
        return super.isIsAllowDelete("DELETE_AREA") ;
    }
    public boolean isAllowRadioAREA(){
        return super.isIsAllowDelete("APPROVAL_AREA") ;
    }
    
    //- ----  quyen doi voi ho gia dinh
    public boolean isAllowUpdateHOUSEHOLD(){
        return super.isIsAllowUpdate("EDIT_HOUSEHOLD") ;
    }
    public boolean isAllowInsertHOUSEHOLD(){
        return super.isIsAllowInsert("ADD_HOUSEHOLD") ;
    }
    public boolean isAllowDeleteHOUSEHOLD(){
        return super.isIsAllowDelete("DELETE_HOUSEHOLD") ;
    }
   
    public boolean isAllowHouseManagement() {
        return allowHouseManagement;
    }

    public boolean isIsCityMode() {
        return isCityMode;
    }

    public List<RadioChannel> getListAllChannel() {
        return listAllChannel;
    }

    public void setListAllChannel(List<RadioChannel> listAllChannel) {
        this.listAllChannel = listAllChannel;
    }

    public long getSelectedChannelId() {
        return selectedChannelId;
    }

    public void setSelectedChannelId(long selectedChannelId) {
        this.selectedChannelId = selectedChannelId;
    }

    public List<Long> getListAreaChannelId() {
        return listAreaChannelId;
    }

    public void setListAreaChannelId(List<Long> listAreaChannelId) {
        this.listAreaChannelId = listAreaChannelId;
    }



    
    
    public void viewMCUTimeline(MCU mcu) {
        selectedMCU = mcu;
        initTimeline();
    
    }
    
    public void viewMCULog(MCU mcu) {
        selectedMCU = mcu;
        initMCULog();
    
    }
    
    public void initTimeline() {
        toDateForTimeline = DateUtil.getEndOfDay(toDateForTimeline);
        fromDateForTimeline = DateUtil.getStartOfDay(fromDateForTimeline);
        MCUState mcuState = areaWS.getMCUStateTimeline(selectedMCU.getMcu_id(), fromDateForTimeline,toDateForTimeline);
        createSecondTimeline(mcuState.getStatusLog());
        createFirstTimeline(mcuState.getPlayStateList(), mcuState.getScheduleList());
    }
    
    public void initMCULog() { 
        try {
            toDateForTimeline = DateUtil.getEndOfDay(toDateForTimeline);
            fromDateForLog = DateUtil.getStartOfDay(fromDateForLog);

            modelLog = new TimelineModel();
            List<MCULog> MCUlogList = new ArrayList<>();
            MCUlogList = areaWS.getMCULogList(selectedMCU.getMcu_id(), fromDateForLog,toDateForTimeline);

            for (MCULog mCULog : MCUlogList) {
                modelLog.add(new TimelineEvent(mCULog, df.parse(mCULog.getCreated_date())));
                //modelLog.add(new TimelineEvent());
                //modelLog.add(new TimelineEvent(mCULog.getCreated_date(), df.parse(mCULog.getCreated_date()), false, ""+mCULog.getLog_index()));
            }
        
        } catch (ParseException ex) {
                Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     
 
      
       //Date time = df.parse(play.getLog_time());  
       // modelLog.add(new TimelineEvent(play.getRec_summary(), time));

            

        

        
    }
    
    // Timeline 
    private TimelineModel modelLog;
    private TimelineModel modelFirst;  // model of the first timeline  
    private TimelineModel modelSecond; // model of the second timeline   
    private boolean aSelected;         // flag if the project A is selected (for test of select() call on the 2. model)  
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date fromDateForTimeline;
    private Date fromDateForLog;
    private Date toDateForTimeline;
 
    @PostConstruct 
    public void init() {
        //createFirstTimeline();
        //createSecondTimeline();
    }
 
    private void createFirstTimeline(List<MCUPlayStateList> playList, List<MCUScheduleList> schedules) {
        modelFirst = new TimelineModel();
        try {
 
            for (MCUPlayStateList play : playList) {
                if (play.getLog_type() == 3 || play.getLog_type()==20) {
                    Date time = df.parse(play.getLog_time());  
                    String type = play.getRec_type()==0?"TT":play.getRec_type()==2?"TS":"FM";
                    modelFirst.add(new TimelineEvent(type + "_" + play.getRec_summary(), time));
                }
            }
            
            for (MCUScheduleList schedule : schedules) {
                boolean isPlayed = false;
                for (MCUPlayStateList play : playList) {
                    if ((play.getLog_type() == 3||play.getLog_type()==20) && play.getRec_id() == schedule.getRec_id()) {
                        Date timeSchedule = df.parse(schedule.getStart_time()); 
                        Date timePlay = df.parse(play.getLog_time()); 
                        
                        long seconds = Math.abs((timeSchedule.getTime()-timePlay.getTime()))/1000;
                        
                        if (seconds < 10) {
                            isPlayed = true;
                            break;
                        }
                    }
                }
                if (!isPlayed) {
                    String type = schedule.getRec_type()==0?"TT":schedule.getRec_type()==2?"TS":"FM";
                    modelFirst.add(new TimelineEvent(type + "_"+ schedule.getRec_summary(), df.parse(schedule.getStart_time()), false, null, "unavailable"));
                }
                
                

            }
        
        } catch (ParseException ex) {
                Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
           
    
    }
    
    private void createSecondTimeline(List<MCUStatusLog> statusList) {
        modelSecond = new TimelineModel();
        
         
        try {
            for (int i = 0; i < statusList.size(); i++) {
                


                    Date startProject = df.parse(statusList.get(i).getMcu_log_time());
                    Date endProject = new Date();
                    if (i < statusList.size()-1) {
                        endProject = df.parse(statusList.get(i+1).getMcu_log_time());
                    } 
                    String status = "available";
                    String operator = SystemConfig.getConfig("operator");
                    if ((operator != null && operator.length()>0)  || SecUser.isSuperAdmin()){
                        status = statusList.get(i).getMcu_status()==1?"available":"unavailable2";
                    }
                    modelSecond.add(new TimelineEvent("", startProject, endProject, false, "Trạng thái kết nối", status));

            }
        
        } catch (ParseException ex) {
                Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
 
       
    }
 
    public void onSelect(TimelineSelectEvent e) {  
        TimelineEvent timelineEvent = e.getTimelineEvent();  
   
        MCULog mcuLog  = (MCULog) timelineEvent.getData();
        listMCURecord = areaWS.getMCURecordList(mcuLog.getLog_index());
        
    }  
 
    public TimelineModel getModelFirst() {
        return modelFirst;
    }
 
    public TimelineModel getModelSecond() {
        return modelSecond;
    }
 
    public class Task implements Serializable {
 
        private String title;
        private String imagePath;
        private boolean period;
 
        public Task(String title, String imagePath, boolean period) {
            this.title = title;
            this.imagePath = imagePath;
            this.period = period;
        }
 
        public String getTitle() {
            return title;
        }
 
        public String getImagePath() {
            return imagePath;
        }
 
        public boolean isPeriod() {
            return period;
        }
    }

    public Date getFromDateForTimeline() {
        return fromDateForTimeline;
    }

    public void setFromDateForTimeline(Date fromDateForTimeline) {
        this.fromDateForTimeline = fromDateForTimeline;
    }

    public Date getToDateForTimeline() {
        return toDateForTimeline;
    }

    public void setToDateForTimeline(Date toDateForTimeline) {
        this.toDateForTimeline = toDateForTimeline;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public TimelineModel getModelLog() {
        return modelLog;
    }

    public void setModelLog(TimelineModel modelLog) {
        this.modelLog = modelLog;
    }

    public List<MCURecord> getListMCURecord() {
        return listMCURecord;
    }

    public Date getFromDateForLog() {
        return fromDateForLog;
    }

    public void setFromDateForLog(Date fromDateForLog) {
        this.fromDateForLog = fromDateForLog;
    }


    // --------------------------------UPLOAD HEADER FILE----------------------------------------
    private String fileName = "";
    private UploadedFile audioFile;
    private File tmpAudioFile;
    private String audioPath = "";
    private int type;
    private long receiveId;
    
    private void saveInputStreamToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];

		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
    }
    
    public void uploadAudioHandler(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        
        String ext;
        try {
            ext = file.getFileName().substring(file.getFileName().lastIndexOf(".") + 1);
            ext = ext.toUpperCase();
        } catch (Exception e) {
            ext = "";
        }

        if (ext == null || ext.length()==0) {

        } else if (!(ext.equals("WAV") || ext.equals("MP3") || ext.equals("AMR") || ext.equals("GIF") || ext.equals("M4A") ||ext.equals("AAC") || ext.equals("FLAC") || ext.equals("OGG"))){
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không được hỗ trợ. Mời tải lên định dạng file WAV/MP3/M4A/AMR/AAC/OGG..."); 
            return;
        }
        
        if(file != null) {
            try {
                String assetsDir = SystemConfig.getConfig("AssetsDir");
                String outputTmpDir = assetsDir+ SystemConfig.getConfig("audioOutputTmp");
                if(!new File(outputTmpDir).exists()){
                    new File(outputTmpDir).mkdirs();
                }
                String timestamp = System.currentTimeMillis()+"";
                String outputPath = outputTmpDir + timestamp + "_"+file.getFileName();
                
                saveInputStreamToFile(file.getInputstream(), outputPath);
                //AudioSystem.write(AudioSystem.getAudioInputStream(new File(convertAudio(audioFile))), AudioFileFormat.Type.WAVE, new File(outputPath));
                
                
                float bitrate = 0;
                if (ext.equals("MP3")) {
                    Mp3File mp3 = new Mp3File(outputPath);
                    bitrate = mp3.getBitrate();
                } else if (ext.equals("WAV") && file.getContentType().compareTo("application/octet-stream") != 0) {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getInputstream());
                    AudioFormat format = audioInputStream.getFormat();
                    int frameSize = format.getFrameSize();
                    float frameRate = format.getFrameRate();
                    bitrate = frameSize * frameRate * 8 /1000;
                }
               
                if (bitrate > Long.parseLong(SystemConfig.getConfig("maxBitRate"))) { 
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Hệ thống chỉ hỗ trợ file có bitrate tối đa " + SystemConfig.getConfig("maxBitRate") + "kbps"); 
                    return;
                }
                
                audioFile = file;
                tmpAudioFile = new File(outputPath);
                fileName = file.getFileName();
                //RequestContext.getCurrentInstance().update("fileName");
                RequestContext.getCurrentInstance().execute("reloadPlayer()");  
                System.out.print("Succesful"+ file.getFileName() + " is uploaded.");
                
                
            } catch (IOException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedTagException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidDataException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    public StreamedContent getPlaybackStream() throws FileNotFoundException, IOException {
                
        if(tmpAudioFile!= null && tmpAudioFile.exists()){
            InputStream is = new FileInputStream(tmpAudioFile);
            String mimeType = URLConnection.guessContentTypeFromStream(is);
            return new DefaultStreamedContent(is, mimeType, tmpAudioFile.getName()," identity;q=1, *;q=0",(int)tmpAudioFile.length());
//        }else if(this.selectedRecord != null && this.selectedRecord.getRec_url()!=null){
//          
//            URLConnection connection = new URL(SystemConfig.getConfig("WSAudioDownload") + selectedRecord.getRec_url().replace("/", "%2f")).openConnection();
//            connection.setRequestProperty("username", SystemConfig.getConfig("AudioWSUser"));
//            connection.setRequestProperty("password", SystemConfig.getConfig("AudioWSPass"));
//            InputStream response = connection.getInputStream();
//       
//            return new DefaultStreamedContent(response, "audio/wav", selectedRecord.getRecFileName());
        }else{
            return null;
        }
    }
     
    public boolean uploadRecordToFileServer(UploadedFile uploadedFile) {
        String ext = uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf(".") + 1);
        ext = ext.toUpperCase();
        File convertedFile = null;
        
        convertedFile = new File(convertAudio(uploadedFile));
        if(!convertedFile.exists()){
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Quá trình xử lý file gặp lỗi");
            return false;
        }
        
        HttpClient client = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(SystemConfig.getConfig("WSAudioUrl"));
        try {
            //Set various attributes
            MultipartEntity multiPartEntity = new MultipartEntity();
            multiPartEntity.addPart("fileDescription", new StringBody(""));
            multiPartEntity.addPart("fileName", new StringBody(convertedFile.getName()));
            InputStreamBody fileBody = new InputStreamBody(new FileInputStream(convertedFile),"application/octect-stream",convertedFile.getName());
            //Prepare payload
            multiPartEntity.addPart("file", fileBody);
            //Set to request body
            
            postRequest.setEntity(multiPartEntity);
            postRequest.addHeader("username", "cmsuser");
            postRequest.addHeader("password", "cmspass!@#");
            //Send request
            HttpResponse response = client.execute(postRequest);
            //Verify response if any
            if (response != null) {
                if (response.getStatusLine().getStatusCode()==200){
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    JsonParser jsonParser = new JsonParser();
                    JsonObject entityJs = (JsonObject)jsonParser.parse(responseString);
                    JsonObject javaResponse= entityJs.get("javaResponse").getAsJsonObject();
                    if (javaResponse!=null)
                    //addedArea.setAudio_path(javaResponse.get("audioUri").getAsString());
                    audioPath = javaResponse.get("audioUri").getAsString();

                    return true;
                }
            }

        } catch (Exception ex) {
            //addedArea.setAudio_path(null);
            audioPath = "";
            ex.printStackTrace();
            return false;
        }
        return false;
    }
     
    public String convertAudio(UploadedFile audioFile){
        String outputFile = null;
        try {
           String timestamp = System.currentTimeMillis()+"";
            String assetsDir = SystemConfig.getConfig("AssetsDir");
            String outputRaw = assetsDir+ SystemConfig.getConfig("audioOutputRaw");
            String outputConverted =assetsDir+ SystemConfig.getConfig("audioOutputConverted");


            File folderConverted = new File(outputConverted);
            File folderRow = new File(outputRaw);
            if(!folderRow.exists()|| !folderRow.isDirectory()){
                folderRow.mkdirs();
            }
            if(!folderConverted.exists()|| !folderConverted.isDirectory()){
                folderConverted.mkdirs();
            }


            String audioFormat = SystemConfig.getConfig("audioAudioFormat");
            String audioCodec = SystemConfig.getConfig("audioCodec");
            String samplingRate = SystemConfig.getConfig("audioSamplingRate");
            
            String outputFileName = timestamp+"_"+audioFile.getFileName().replaceAll("[^A-Za-z0-9\\.]","");
            String outputFilePath = outputRaw+outputFileName;
            saveInputStreamToFile(audioFile.getInputstream(), outputFilePath);
            File f = new File(outputFilePath);
            if(!f.exists()){
                throw new FileNotFoundException("upload failed");
            }

            Attributes alaw = new Attributes(audioFormat, audioCodec, Integer.parseInt(samplingRate), 1);
            outputFile = outputConverted+outputFileName+ "."+ alaw.getFormat();
            Transcoder.transcode(f.getAbsolutePath(), outputFile, alaw);
       } catch (Exception e) {
           e.printStackTrace();
           ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Xảy ra lỗi trong quá trình convert file: " + e);
       }
        
        if(outputFile!=null && new File(outputFile).exists()){
            return outputFile;            
        }else{
            return null;
        }
 
    }
    
      //////////////////////////////////////////////////////////////////////////////
    // Ai Camera
    //- ----  quyen doi voi ai camera
    public boolean isAllowUpdateAiCamera() {
        return super.isIsAllowUpdate("EDIT_AICAMERA");
    }

    public boolean isAllowInsertAiCamera() {
        return super.isIsAllowInsert("ADD_AICAMERA");
    }

    public boolean isAllowDeleteAiCamera() {
        return super.isIsAllowDelete("DELETE_AICAMERA");
    }

    public boolean isAllowViewAiCamera() {
        return super.isIsAllowView("VIEW_AICAMERA");
    }

    public boolean isAllowApprovalAiCamera() {
        return super.isIsAllowApproval("APPROVAL_AICAMERA");
    }

    public List<AiCamera> getListAiCameraByArea() {
        return listAiCameraByArea;
    }

    public void setListAiCameraByArea(List<AiCamera> listAiCameraByArea) {
        this.listAiCameraByArea = listAiCameraByArea;
    }

    public AiCamera getSelectedAiCamera() {
        return selectedAiCamera;
    }

    public void setSelectedAiCamera(AiCamera selectedAiCamera) {
        this.selectedAiCamera = selectedAiCamera;
    }

    public PersistAction getCameraActtionFlag() {
        return cameraActtionFlag;
    }

    public void setCameraActtionFlag(PersistAction cameraActtionFlag) {
        this.cameraActtionFlag = cameraActtionFlag;
    }

    public void updateMCUCameraEvent(AiCamera aiCamera) {
        selectedAiCamera = aiCamera;
        listAiCameraByArea = areaWS.getAiCameraListByArea(selectedArea.getAreaId());
        List<AiCameraMCU> listAiCameraMCUs = areaWS.getMCUByAICameraIdByCamId(aiCamera.getCam_id());

        listMCUForAICameraDirections = new ArrayList<>();
        listMCUForAICameraDirections.add(new MCUForAICameraDirection(0));
        listMCUForAICameraDirections.add(new MCUForAICameraDirection(1));
        listMCUForAICameraDirections.add(new MCUForAICameraDirection(2));
        listMCUForAICameraDirections.add(new MCUForAICameraDirection(3));

        for (AiCameraMCU aiCameraMCU : listAiCameraMCUs) {
            try {
                listMCUForAICameraDirections.get(aiCameraMCU.getDirection()).setMcu(aiCameraMCU.getMcu_id());
            } catch (Exception ex) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Xảy ra lỗi: " + ex.getMessage());
            }
        }
    }

    public void viewCameraEvent(AiCamera aiCamera) {
        cameraActtionFlag = PersistAction.VIEW;
        selectedAiCamera = aiCamera;
    }

    public void addCameraEvent() {
        cameraActtionFlag = PersistAction.CREATE;
        selectedAiCamera = new AiCamera();
    }

    public void updateCameraEvent(AiCamera aiCamera) {
        cameraActtionFlag = PersistAction.UPDATE;
        selectedAiCamera = new AiCamera(aiCamera);
    }

    public void deleteCameraEvent(AiCamera aiCamera) {
        cameraActtionFlag = PersistAction.DELETE;
        selectedAiCamera = aiCamera;
    }

    public void saveCamera(ActionEvent evt) {
        selectedAiCamera.setAreaId(selectedArea.getAreaId());
        DataResponse response = null;
        if (cameraActtionFlag == PersistAction.CREATE) {
            response = areaWS.addAiCamera(selectedAiCamera);

        } else if (cameraActtionFlag == PersistAction.UPDATE) {
            response = areaWS.editAiCamera(selectedAiCamera);
        }
        listAiCameraByArea = areaWS.getAiCameraListByArea(this.selectedArea.getAreaId());

        if (response != null && response.getCode() == 200) {
            ClientMessage.info(response.getCodeDescVn());
        } else {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, response.getCodeDescVn());
        }
    }

    public void deleteCamera(ActionEvent evt) {
        DataResponse response = areaWS.deleteAiCamera(selectedAiCamera.getCam_id());

        if (response.getCode() == 200) {
            ClientMessage.info(response.getCodeDescVn());
        } else {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, response.getCodeDescVn());
        }
    }

    //////////////////////////////////////////////////
    // Ai Camera MCU
    public void updateCameraMCU() {
        DataResponse response = areaWS.setMCUForAICamera(selectedAiCamera.getCam_id(), listMCUForAICameraDirections);

        if (response.getCode() == 200) {
            ClientMessage.info(response.getCodeDescVn());
        } else {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, response.getCodeDescVn());
        }
    }

    public List<MCUForAICameraDirection> getListMCUForAICameraDirections() {
        return listMCUForAICameraDirections;
    }

    public void setListMCUForAICameraDirections(List<MCUForAICameraDirection> listMCUForAICameraDirections) {
        this.listMCUForAICameraDirections = listMCUForAICameraDirections;
    }

    public class MCUForAICameraDirection {

        public long mcu;
        public int direction;

        public MCUForAICameraDirection() {
        }

        public MCUForAICameraDirection(long mcu, int direction) {
            this.mcu = mcu;
            this.direction = direction;
        }

        public MCUForAICameraDirection(int direction) {
            this.direction = direction;
        }

        public long getMcu() {
            return mcu;
        }

        public void setMcu(long mcu) {
            this.mcu = mcu;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

    }
    
        /////////////////////////////////////////
    // Ai Camera Slot
    public class SlotForAiCamera {

        private long start_time;
        private long end_time;
        private int type; // 1- Live, 2- Off

        public SlotForAiCamera() {
        }

        public SlotForAiCamera(int type) {
            this.type = type;
        }

        public SlotForAiCamera(long start_time, long end_time, int type) {
            this.start_time = start_time;
            this.end_time = end_time;
            this.type = type;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public String getStartTimeAdded() {
        return startTimeAdded;
    }

    public void setStartTimeAdded(String startTimeAdded) {
        this.startTimeAdded = startTimeAdded;
    }

    public String getEndTimeAdded() {
        return endTimeAdded;
    }

    public void setEndTimeAdded(String endTimeAdded) {
        this.endTimeAdded = endTimeAdded;
    }

    public String getNotifyTimeLive() {
        return notifyTimeLive;
    }

    public void setNotifyTimeLive(String notifyTimeLive) {
        this.notifyTimeLive = notifyTimeLive;
    }

    public String getNotifyTimeOff() {
        return notifyTimeOff;
    }

    public void setNotifyTimeOff(String notifyTimeOff) {
        this.notifyTimeOff = notifyTimeOff;
    }

    public int getTypeSlot() {
        return typeSlot;
    }

    public void setTypeSlot(int typeSlot) {
        this.typeSlot = typeSlot;
    }

    public List<SlotForAiCamera> getListSlotForAiCamerasLive() {
        return listSlotForAiCamerasLive;
    }

    public void setListSlotForAiCamerasLive(List<SlotForAiCamera> listSlotForAiCamerasLive) {
        this.listSlotForAiCamerasLive = listSlotForAiCamerasLive;
    }

    public List<SlotForAiCamera> getListSlotForAiCamerasOff() {
        return listSlotForAiCamerasOff;
    }

    public void setListSlotForAiCamerasOff(List<SlotForAiCamera> listSlotForAiCamerasOff) {
        this.listSlotForAiCamerasOff = listSlotForAiCamerasOff;
    }

    public List<SlotForAiCamera> getListSlotForAiCamerasAll() {
        return listSlotForAiCamerasAll;
    }

    public void setListSlotForAiCamerasAll(List<SlotForAiCamera> listSlotForAiCamerasAll) {
        this.listSlotForAiCamerasAll = listSlotForAiCamerasAll;
    }

    public void updateAiCameraSlotEvent(AiCamera aiCamera) {
        selectedAiCamera = aiCamera;

        // init time in DB
        List<AiCameraSlot> listSlotForAiCameras = areaWS.getTimeSlotByAICameraId(aiCamera.getCam_id());
        convertTimeSlotAiCamera(listSlotForAiCameras);

    }

    private void convertTimeSlotAiCamera(List<AiCameraSlot> listSlotForAiCameras) {
        listSlotForAiCamerasAll = new ArrayList<>();
        listSlotForAiCamerasLive = new ArrayList<>();
        listSlotForAiCamerasOff = new ArrayList<>();
        startTimeAdded = "";
        endTimeAdded = "";
        notifyTimeLive = "";
        notifyTimeOff = "";

        for (AiCameraSlot aiCameraSlot : listSlotForAiCameras) {
            if (aiCameraSlot.getType() == 1) {
                listSlotForAiCamerasLive.add(new SlotForAiCamera(aiCameraSlot.getStart_time(), aiCameraSlot.getEnd_time(), 1));
                if (notifyTimeLive != "") {
                    notifyTimeLive += ", " + convertSecondsTimeToTime(aiCameraSlot.getStart_time()) + "-" + convertSecondsTimeToTime(aiCameraSlot.getEnd_time());
                } else {
                    notifyTimeLive = convertSecondsTimeToTime(aiCameraSlot.getStart_time()) + "-" + convertSecondsTimeToTime(aiCameraSlot.getEnd_time());
                }
            } else if (aiCameraSlot.getType() == 2) {
                listSlotForAiCamerasOff.add(new SlotForAiCamera(aiCameraSlot.getStart_time(), aiCameraSlot.getEnd_time(), 2));

                if (notifyTimeOff != "") {
                    notifyTimeOff += ", " + convertSecondsTimeToTime(aiCameraSlot.getStart_time()) + "-" + convertSecondsTimeToTime(aiCameraSlot.getEnd_time());
                } else {
                    notifyTimeOff = convertSecondsTimeToTime(aiCameraSlot.getStart_time()) + "-" + convertSecondsTimeToTime(aiCameraSlot.getEnd_time());
                }
            }
        }

    }

    public void openDlUpdateSlotAiCameraEvent(int type) {
        this.typeSlot = type;
        startTimeAdded = "";
        endTimeAdded = "";
    }

    public void saveSlotTimeEvent() {
        String recPlayTimeStringForRadio;
        if (this.typeSlot == 1) {
            recPlayTimeStringForRadio = this.notifyTimeLive;
        } else {
            recPlayTimeStringForRadio = this.notifyTimeOff;
        }

        try {
            if (startTimeAdded == null || startTimeAdded.equals("")) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa chọn giờ bắt đầu!");
                return;
            }
            if (endTimeAdded == null || endTimeAdded.equals("")) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa chọn giờ kết thúc!");
                return;
            }

            if (startTimeAdded.compareTo(endTimeAdded) >= 0) {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian kết thúc phải sau thời gian bắt đầu! Vui lòng chọn lại");
                return;
            }

            if (recPlayTimeStringForRadio == null || recPlayTimeStringForRadio.length() < 13) {

                recPlayTimeStringForRadio = startTimeAdded + "-" + endTimeAdded;

            } else {

                List<Integer> startList = new ArrayList<>();
                List<Integer> endList = new ArrayList<>();
                String timeString = recPlayTimeStringForRadio.replaceAll("\\s", "");
                List<String> timeList = Arrays.asList(timeString.split(","));

                recPlayTimeStringForRadio = timeList.toString().substring(1, timeList.toString().length() - 1);

                for (String time : timeList) {

                    String start = Arrays.asList(time.split("-")).get(0);
                    String end = Arrays.asList(time.split("-")).get(1);

                    startList.add(convertStringToSecondsTime(start));
                    endList.add(convertStringToSecondsTime(end));

                }

                int startAdded = convertStringToSecondsTime(startTimeAdded);
                int endAdded = convertStringToSecondsTime(endTimeAdded);

                for (int i = 0; i < startList.size(); i++) {
                    if (!((startAdded <= startList.get(i) && endAdded <= startList.get(i)) || (startAdded >= endList.get(i) && endAdded >= endList.get(i)))) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian trùng, chờm với thời gian đã chọn! Vui lòng chọn lại");
                        return;
                    }
                    if (startList.get(i) == -1 || endList.get(i) == -1) {
                        ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian hập vào không hợp lệ! Vui lòng chọn lại");
                        return;
                    }
                }

                recPlayTimeStringForRadio = recPlayTimeStringForRadio + ", " + startTimeAdded + "-" + endTimeAdded;

            }
        } catch (Exception ex) {
            startTimeAdded = "";
            endTimeAdded = "";
            if (this.typeSlot == 1) {
                this.notifyTimeLive = "";
            } else {
                this.notifyTimeOff = "";
            }

            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian hập vào không hợp lệ! Vui lòng chọn lại");
        }

        if (this.typeSlot == 1) {
            this.notifyTimeLive = recPlayTimeStringForRadio;
        } else {
            this.notifyTimeOff = recPlayTimeStringForRadio;
        }
    }

    private int convertStringToSecondsTime(String time) {
        int hh = Integer.parseInt(Arrays.asList(time.split(":")).get(0));
        int mm = Integer.parseInt(Arrays.asList(time.split(":")).get(1));
        int ss = Integer.parseInt(Arrays.asList(time.split(":")).get(2));

        if (hh > 23 || mm > 59 || ss > 59) {
            return -1;
        }
        return (hh * 3600 + mm * 60 + ss);
    }

    private String convertSecondsTimeToTime(long time) {
        time = time % (3600 * 24);
        String result = "";
        int hours = (int) (time / 3600);
        if (hours < 10) {
            result += "0" + hours;
        } else {
            result += hours;
        }
        int minute = (int) ((time - hours * 3600) / 60);
        if (minute < 10) {
            result += ":0" + minute;
        } else {
            result += ":" + minute;
        }
        int seconds = (int) (time - hours * 3600 - minute * 60);
        if (seconds < 10) {
            result += ":0" + seconds;
        } else {
            result += ":" + seconds;
        }
        return result;
    }

    public void updateNotifyTime() {

        listSlotForAiCamerasAll = new ArrayList<>();
        listSlotForAiCamerasLive = new ArrayList<>();
        listSlotForAiCamerasOff = new ArrayList<>();

        try {
            String timeString = this.notifyTimeLive.replaceAll("\\s", "");
            List<String> timeList = Arrays.asList(timeString.split(","));

            if (this.notifyTimeLive != "") {
                for (String time : timeList) {

                    String start = Arrays.asList(time.split("-")).get(0);
                    String end = Arrays.asList(time.split("-")).get(1);

                    if (convertStringToSecondsTime(start) != -1 && convertStringToSecondsTime(end) != -1) {
                        listSlotForAiCamerasLive.add(new SlotForAiCamera(convertStringToSecondsTime(start), convertStringToSecondsTime(end), 1));
                    }
                }
            }

            timeString = this.notifyTimeOff.replaceAll("\\s", "");
            timeList = Arrays.asList(timeString.split(","));

            if (this.notifyTimeOff != "") {
                for (String time : timeList) {

                    String start = Arrays.asList(time.split("-")).get(0);
                    String end = Arrays.asList(time.split("-")).get(1);

                    if (convertStringToSecondsTime(start) != -1 && convertStringToSecondsTime(end) != -1) {
                        listSlotForAiCamerasOff.add(new SlotForAiCamera(convertStringToSecondsTime(start), convertStringToSecondsTime(end), 2));
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(RecordController.class.getName()).log(Level.SEVERE, null, ex);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Thời gian hập vào không hợp lệ! Vui lòng chọn lại");
            return;
        }

        listSlotForAiCamerasAll.addAll(listSlotForAiCamerasLive);
        listSlotForAiCamerasAll.addAll(listSlotForAiCamerasOff);

        if (listSlotForAiCamerasAll.size() == 0) {
            listSlotForAiCamerasAll.add(new SlotForAiCamera(0, 86399, 1));
        }

        DataResponse response = areaWS.setTimeSlotForAICamera(selectedAiCamera.getCam_id(), listSlotForAiCamerasAll);

        if (response != null && response.getCode() == 200) {
            ClientMessage.info(response.getCodeDescVn());
        } else {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, response.getCodeDescVn());
        }
    }
    
}
