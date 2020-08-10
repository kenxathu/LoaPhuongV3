/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import vn.mobifone.loaphuong.model.AreaModel;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.DataResponse;
import org.primefaces.event.FileUploadEvent;
import vn.mobifone.loaphuong.entity.AreaNews;
import vn.mobifone.loaphuong.entity.Category;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import vn.mobifone.loaphuong.entity.Servey;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.TSPermission;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.webservice.NewsWebservice;
import vn.mobifone.loaphuong.webservice.ServeyWebservice;





/**
 *
 * @author cuong.trinh
 */
@ManagedBean(name = "ServeyController")
@ViewScoped
public class ServeyController extends TSPermission implements Serializable  {
    private TreeNode selectedNode;
    private AreaTree selectedArea;
    private AreaTree areaRoot;
    private AreaTree addedArea;
    
    
    private AreaModel areaModel;
    private final String mediaServer = SystemConfig.getConfig("ReadAsset");
    

    //News
    private Servey selectedServey;
    private PersistAction serveyActtionFlag;
    
    //Servey
    private List<Servey> listServey;
    private ServeyWebservice serveyWS;

    //time filter
    private Date fromDate;
    private Date toDate;
    
    
    //private final String APP_PATH = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");

        // Operation Flag -- Enum
    public static enum PersistAction {

        SELECT,
        CREATE,
        DELETE,
        VIEW,
        APPROVE,
        UPDATE
    }
    
    //@ManagedProperty("#{documentService}")
    private DocumentService service;
     


    public ServeyController() {
        service = new DocumentService();
        areaRoot = service.createAreaTree();
        serveyWS = new ServeyWebservice();

        
        //areaRoot.getChildren().get(0).setSelected(true);
        areaRoot.getChildren().get(0).setExpanded(true);
        selectedArea = (AreaTree) areaRoot.getChildren().get(0);
        
        toDate = new Date();
        Calendar cal = Calendar.getInstance();  
        cal.add(Calendar.MONTH, -1);  
        fromDate = cal.getTime();
        
        loadServey();
        
        areaModel = new AreaModel();
        serveyActtionFlag  = PersistAction.SELECT;
        
        selectedServey = new Servey();
    }
 
    public TreeNode getSelectedNode() {
        return selectedNode;
    }
 
    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
        
    
        System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()" + "select note " + selectedArea.getAreaName());
        
 
    }                
 
    public void setService(DocumentService service) {
        this.service = service;
    }

    public void addNewsEvent() { 
        serveyActtionFlag = PersistAction.CREATE;
        System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()" + " add news " );
        selectedServey = new Servey();
        
 

    }  
        
    public void viewNewsEvent(Servey servey) { 
        serveyActtionFlag = PersistAction.VIEW;
        selectedServey = servey;
    }  
   
    public void editNewsEvent(Servey servey) { 
        serveyActtionFlag = PersistAction.UPDATE;
        selectedServey = servey;

    } 
    
    public void approveNewsEvent(Servey servey) { 
        serveyActtionFlag = PersistAction.APPROVE;
        selectedServey = servey;
    } 
    
    public void deleteNewsEvent(Servey servey) { 
        serveyActtionFlag = PersistAction.DELETE;
        selectedServey = servey;
    } 
    
    public void cancelNews(ActionEvent evt) { 
        
        DataResponse response = new DataResponse();
            // Huy tin tuc
//           response = coreWSNews.updateNewsStatus(-1, selectedServey.getId());
//           if (response.getCode() == 200) {
//               listServey = coreWSNews.getListNewsByArea(selectedArea.getAreaId(), selectedArea.getAreaType()== 3 ? true:false, fromDate, toDate);
//           }
    } 
    
    public void saveNews(ActionEvent evt){
        System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()"    +  " save news");
        try {
            DataResponse response = new DataResponse();
            if (serveyActtionFlag == PersistAction.UPDATE) {
               
               // response = serveyWS.executeNewsJson(selectedServey, 2);
                
            } else if (serveyActtionFlag == PersistAction.CREATE) {
               

                //response = serveyWS.executeNewsJson(selectedServey, 1);
                
                
            } else if (serveyActtionFlag == PersistAction.APPROVE) {
               // response = serveyWS.approveRecordNews(2, selectedServey.getId());
                
                
            } else if (serveyActtionFlag == PersistAction.DELETE) {
                
                if (selectedServey.getStatus() == 0) {   //Xoa tin tuc
                    //response = serveyWS.updateNewsStatus(-2, selectedServey.getId());
                } else {             // Huy tin tuc
                   // response = serveyWS.updateNewsStatus(-1, selectedServey.getId());
                }
                
            }
            
            if (response.getCode() == 200) {
                listServey = serveyWS.getListServeyByArea(selectedArea.getAreaId());
                
            }
            
        } catch (Exception ex) {
            Logger.getLogger(ServeyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void deleteNode() { 
        System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()"    +  " delete note");
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
    }
    
    public void changeFilterTimeEvent(ActionEvent evt) { 
        if (fromDate.after(toDate)) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Chọn ngày không hợp lệ! Ngày bắt đầu phải trước ngày kết thúc");
            return;
        }
        loadServey();
    }

    public void loadServey() {
        
        try {
            if (selectedArea != null) {
                listServey = serveyWS.getListServeyByArea(SecUser.getUserLogged().getAreaId());
            }
        } catch (Exception ex) {
                Logger.getLogger(ServeyController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getFilePath(){
            //FileInputStream file = new FileInputStream(image.getInputstream());
                return "";
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

    public PersistAction getNewsActtionFlag() {
        return serveyActtionFlag;
    }

    public void setNewsActtionFlag(PersistAction newsActtionFlag) {
        this.serveyActtionFlag = newsActtionFlag;
    }
    
    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public void upload1(String subject){
        System.out.println(""+ subject);
    }

    public List<Servey> getListServey() {
        return listServey;
    }

    public void setListServey(List<Servey> listServey) {
        this.listServey = listServey;
    }

   
    //- ----  quyen doi voi ho ban tin
    public boolean isAllowUpdateNEW(){
        return super.isIsAllowUpdate("EDIT_NEWS") ;
    }
    public boolean isAllowInsertNEW(){
        return super.isIsAllowInsert("ADD_NEWS") ;
    }
    public boolean isAllowDeleteNEW(){
        return super.isIsAllowDelete("DELETE_NEWS") ;
    }
     public boolean isAllowApprovalNEW(){
        return super.isIsAllowApproval("APPROVAL_NEWS") ;
    }
    
     //- ----  quyen doi voi ho gia dinh
    public boolean isAllowUpdateRECORD(){
        return super.isIsAllowUpdate("EDIT_RECORD") ;
    }
    public boolean isAllowInsertRECORD(){
        return super.isIsAllowInsert("ADD_RECORD") ;
    }
    public boolean isAllowDeleteRECORD(){
        return super.isIsAllowDelete("DELETE_RECORD") ;
    }
    
    public boolean isAllowApprovalRECORD(){
        return super.isIsAllowApproval("APPROVAL_RECORD") ;
    }

}
