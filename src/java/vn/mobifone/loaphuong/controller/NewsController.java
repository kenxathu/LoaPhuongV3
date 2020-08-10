/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import vn.mobifone.loaphuong.model.AreaModel;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.entity.DataResponse;
import org.primefaces.event.FileUploadEvent;
import vn.mobifone.loaphuong.entity.AreaNews;
import vn.mobifone.loaphuong.entity.Category;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.SystemConfig;
import vn.mobifone.loaphuong.lib.TSPermission;
import vn.mobifone.loaphuong.webservice.NewsWebservice;





/**
 *
 * @author cuong.trinh
 */
@ManagedBean(name = "NewsController")
@ViewScoped
public class NewsController extends TSPermission implements Serializable  {
    private TreeNode selectedNode;
    private AreaTree selectedArea;
    private AreaTree areaRoot;
    private AreaTree addedArea;
    private NewsWebservice coreWSNews;
    
    private AreaModel areaModel;
    private String mediaServer;
    

    //News
    private List<AreaNews> listNews;
    private AreaNews selectedNews;
    private PersistAction newsActtionFlag;
    private UploadedFile image;
    private UploadedFile video;
    private List<Category> listNewsCategory;
    private String imageContent;
    private String videoContent;
    private StreamedContent imageStreamContent;
    private String videoFileName;
    
//    private Day day;
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
     


    public NewsController() {
        service = new DocumentService();
        areaRoot = service.createAreaTree();
        coreWSNews = new NewsWebservice();

        mediaServer = SystemConfig.getConfig("ReadAsset");
        //areaRoot.getChildren().get(0).setSelected(true);
        areaRoot.getChildren().get(0).setExpanded(true);
        selectedArea = (AreaTree) areaRoot.getChildren().get(0);
        
        toDate = new Date();
        Calendar cal = Calendar.getInstance();  
        cal.add(Calendar.MONTH, -1);  
        fromDate = cal.getTime();
        
        loadNews();
        
        areaModel = new AreaModel();
        newsActtionFlag  = PersistAction.SELECT;
        
        selectedNews = new AreaNews();
        
        listNewsCategory = coreWSNews.getListNewsCateogy(1);

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
        newsActtionFlag = PersistAction.CREATE;
        System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()" + " add news " );
        selectedNews = new AreaNews();
        
        selectedNews.setArea_id(selectedArea.getAreaId());
        selectedNews.setFull_description("");
        selectedNews.setCategory_id(1);
        selectedNews.setNotify_flag(1);
        image = null;
        imageContent=null;
        videoFileName = null;
        video = null;
    }  
        
    public void viewNewsEvent(AreaNews news) { 
        newsActtionFlag = PersistAction.VIEW;
        selectedNews = news;
        imageContent = mediaServer + selectedNews.getUrl();
    }  
   
    public void editNewsEvent(AreaNews news) { 
        newsActtionFlag = PersistAction.UPDATE;
        selectedNews = news;
        imageContent = mediaServer + selectedNews.getUrl();
        image = null;
    } 
    
    public void approveNewsEvent(AreaNews news) { 
        newsActtionFlag = PersistAction.APPROVE;
        selectedNews = news;
        imageContent = mediaServer + selectedNews.getUrl();
    } 
    
    public void deleteNewsEvent(AreaNews news) { 
        newsActtionFlag = PersistAction.DELETE;
        selectedNews = news;
    } 
    
    public void cancelNews(ActionEvent evt) { 
        
        DataResponse response = new DataResponse();
            // Huy tin tuc
           response = coreWSNews.updateNewsStatus( -1, selectedNews.getId());
           if (response.getCode() == 200) {
               listNews = coreWSNews.getListNewsByArea(selectedArea.getAreaId(), true, fromDate, toDate);
           }
    } 
    
    public void saveNews(ActionEvent evt){
        System.out.println("vn.mobifone.controller.MediaController.displaySelectedSingle()"    +  " save news");
        try {
            DataResponse response = new DataResponse();
            if (newsActtionFlag == PersistAction.UPDATE) {
                if (selectedNews.getFull_description() == null || selectedNews.getFull_description().equals("")) {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa nhập nội dung tin tức!");
                    return;
                }
                if (video != null) {
                    uploadVideo(video);
                }
                if (image != null) {
                    uploadPhoto(image);
                }
                response = coreWSNews.executeNewsJson(selectedNews, 2);
                
            } else if (newsActtionFlag == PersistAction.CREATE) {
                if (selectedNews.getFull_description() == null || selectedNews.getFull_description().equals("")) {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa nhập nội dung tin tức!");
                    return;
                }
                if (video != null) {
                    uploadVideo(video);
                }
                if (image != null) {
                    uploadPhoto(image);
                } else {
                    ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "Bạn chưa tải lên ảnh bìa cho tin tức!");
                    return;
                }

                response = coreWSNews.executeNewsJson(selectedNews, 1);
                
                
            } else if (newsActtionFlag == PersistAction.APPROVE) {
                response = coreWSNews.approveRecordNews(2, selectedNews.getId());
                
                
            } else if (newsActtionFlag == PersistAction.DELETE) {
                
                if (selectedNews.getStatus() == 0) {   //Xoa tin tuc
                    response = coreWSNews.updateNewsStatus( -2, selectedNews.getId());
                } else {             // Huy tin tuc
                    response = coreWSNews.updateNewsStatus( -1, selectedNews.getId());
                }
                
            }
            
            if (response.getCode() == 200) {
                listNews = coreWSNews.getListNewsByArea(selectedArea.getAreaId(), true, fromDate, toDate);
                
            }
            
        } catch (Exception ex) {
            Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
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
        loadNews();
    }

    public void loadNews() {
        
        try {
            if (selectedArea != null) {
                listNews = coreWSNews.getListNewsByArea(selectedArea.getAreaId(), true, fromDate, toDate);
            }
        } catch (Exception ex) {
                Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getFilePath(){
            //FileInputStream file = new FileInputStream(image.getInputstream());
                return "";
    }
    
    public void eventUploadAvar(FileUploadEvent event) {
            String ext = event.getFile().getFileName();
            try {
                ext = ext.substring(ext.lastIndexOf(".") + 1);
                ext = ext.toUpperCase();
            } catch (Exception e) {
                ext = "";
            }
            
            if (ext == null || ext.length()==0 || !(ext.equals("PNG") || ext.equals("JPG") || ext.equals("JPEG") || ext.equals("GIF"))){
                ClientMessage.error("File không được hỗ trợ. Mời tải lên định dạng file JPG/JPEG/PNG/GIF!");
                //ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không được hỗ trợ. Mời tải lên định dạng file JPG/JPEG/PNG/GIF!");
                return;
            } else {
                try {
 
                    image = event.getFile();
                    selectedNews.setImageName(image.getFileName());
                    imageContent = "data:image/png;base64," + Base64.getEncoder().encodeToString(image.getContents());
                    
                    imageStreamContent = new DefaultStreamedContent(image.getInputstream(), "image/png");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
    }
    
    public void eventUploadVideo(FileUploadEvent event) {
            String ext = event.getFile().getFileName();
            try {
                ext = ext.substring(ext.lastIndexOf(".") + 1);
                ext = ext.toUpperCase();
            } catch (Exception e) {
                ext = "";
            }
            
            if (ext == null || ext.length()==0 || !(ext.equals("OGG") || ext.equals("MP4") || ext.equals("WebM"))){
                ClientMessage.error("File không được hỗ trợ. Mời tải lên định dạng file MP4/OGG/WebM!");
                //ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không được hỗ trợ. Mời tải lên định dạng file JPG/JPEG/PNG/GIF!");
                return;
            } else {
           
                    
                    video = event.getFile();
                    videoFileName = video.getFileName();
                    videoContent = Base64.getEncoder().encodeToString(video.getContents());
                    //selectedNews.setImageName(image.getFileName());
                    //imageContent = Base64.getEncoder().encodeToString(image.getContents());
                    
                    //imageStreamContent = new DefaultStreamedContent(image.getInputstream(), "image/png");
        
                
            }
    }

    private void saveToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
                File f = new File(target);
                f.setExecutable(true, false);
                f.setReadable(true, false);
		out = new FileOutputStream(f);
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
    }
    
    public boolean uploadVideo(UploadedFile imageFile){
            String ext = imageFile.getFileName();
            try {
                ext = ext.substring(ext.lastIndexOf(".") + 1);
                ext = ext.toUpperCase();
            } catch (Exception e) {
                ext = "";
            }
            
            if (ext == null || ext.length()==0) {
                
            } else if (ext.equals("MP4") || ext.equals("OGG") || ext.equals("WebM")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                java.util.Date date = java.util.Calendar.getInstance().getTime();
                String time = sdf.format(date);
                //String appPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
                String imgPath =SystemConfig.getConfig("AssetsDir") + "resource/images/";
                //boolean isCheckFolder=new File(APP_PATH + "/resource/images/"
                //        + "").mkdirs();
                boolean isCheckFolder=new File(imgPath).mkdirs();
                try {
                   // FileUtils.copyFile(imageFile, targetFile);
                   saveToFile(imageFile.getInputstream(), imgPath + time +"_video_"+ imageFile.getFileName());
                   selectedNews.setImage("/resource/images/"+ time +"_video_"+ imageFile.getFileName());
                   Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
                    //add owners permission
                   perms.add(PosixFilePermission.OWNER_READ);
                   perms.add(PosixFilePermission.OWNER_WRITE);
                   perms.add(PosixFilePermission.OWNER_EXECUTE);
                    //add group permissions
                   perms.add(PosixFilePermission.GROUP_READ);
                   perms.add(PosixFilePermission.GROUP_EXECUTE);
                    //add others permissions
                   perms.add(PosixFilePermission.OTHERS_READ);
                   perms.add(PosixFilePermission.OTHERS_EXECUTE);
                   Files.setPosixFilePermissions(Paths.get(imgPath + time +"_video_"+ imageFile.getFileName()), perms);
                   return true;
                } catch (IOException ex) {
                    Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } else {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không được hỗ trợ. Mời tải lên định dạng file MP4/OGG/WebM!");
               
                
            }
        return  false;
        
    }
    
    public boolean uploadPhoto(UploadedFile imageFile){
            String ext = imageFile.getFileName();
            try {
                ext = ext.substring(ext.lastIndexOf(".") + 1);
                ext = ext.toUpperCase();
            } catch (Exception e) {
                ext = "";
            }
            
            if (ext == null || ext.length()==0) {
                
            } else if (ext.equals("PNG") || ext.equals("JPG") || ext.equals("JPEG") || ext.equals("GIF")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                java.util.Date date = java.util.Calendar.getInstance().getTime();
                String time = sdf.format(date);
                //String appPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
                String imgPath =SystemConfig.getConfig("AssetsDir") + "resource/images/";
                //boolean isCheckFolder=new File(APP_PATH + "/resource/images/"
                //        + "").mkdirs();
                boolean isCheckFolder=new File(imgPath).mkdirs();
                try {
                   // FileUtils.copyFile(imageFile, targetFile);
                   saveToFile(imageFile.getInputstream(), imgPath + time +"_"+ imageFile.getFileName());
                   selectedNews.setUrl("/resource/images/"+ time +"_"+ imageFile.getFileName());
                   Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
                    //add owners permission
                   perms.add(PosixFilePermission.OWNER_READ);
                   perms.add(PosixFilePermission.OWNER_WRITE);
                   perms.add(PosixFilePermission.OWNER_EXECUTE);
                    //add group permissions
                   perms.add(PosixFilePermission.GROUP_READ);
                   perms.add(PosixFilePermission.GROUP_EXECUTE);
                    //add others permissions
                   perms.add(PosixFilePermission.OTHERS_READ);
                   perms.add(PosixFilePermission.OTHERS_EXECUTE);
                   Files.setPosixFilePermissions(Paths.get(imgPath + time +"_"+ imageFile.getFileName()), perms);
                   return true;
                } catch (IOException ex) {
                    Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } else {
                ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, "File không được hỗ trợ. Mời tải lên định dạng file JPG/JPEG/PNG/GIF!");
               
                
            }
        return  false;
        
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
        return newsActtionFlag;
    }

    public void setNewsActtionFlag(PersistAction newsActtionFlag) {
        this.newsActtionFlag = newsActtionFlag;
    }

    public List<AreaNews> getListNews() {
        return listNews;
    }

    public void setListNews(List<AreaNews> listNews) {
        this.listNews = listNews;
    }

    public AreaNews getSelectedNews() {
        return selectedNews;
    }

    public void setSelectedNews(AreaNews selectedNews) {
        this.selectedNews = selectedNews;
    }

    public String getVideoFileName() {
        return videoFileName;
    }

    public void setVideoFileName(String videoFileName) {
        this.videoFileName = videoFileName;
    }



    public UploadedFile getImage() {
        return image;
    }

    public void setImage(UploadedFile image) {
        this.image = image;
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

   
    public List<Category> getListNewsCategory() {
        return listNewsCategory;
    }

    public void setListNewsCategory(List<Category> listNewsCategory) {
        this.listNewsCategory = listNewsCategory;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public StreamedContent getImageStreamContent() {
        return imageStreamContent;
    }

    public void setImageStreamContent(StreamedContent imageStreamContent) {
        this.imageStreamContent = imageStreamContent;
    }

    public TreeNode[] getListAreaSelectedFromID(List<Long> listAreaId){
        TreeNode[] result = new TreeNode[listAreaId.size()];
        for (int i = 0; i < listAreaId.size(); i++) {
            for (AreaTree area : service.getListArea()) {
                if (area.getAreaId() == listAreaId.get(i)) {
                    result[i] = (TreeNode)area;
                }
            }
        }
        
        return result;
    }
    
    public void uploadAssets() {
        try{
            String fileObj= FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()  
           .get("file");  
            String filename= FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()  
           .get("filename"); 
            byte dearr[] = Base64.getDecoder().decode(fileObj);
            //String imgPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resource/images";
            String imgPath = SystemConfig.getConfig("AssetsDir") + "resource/images";
            boolean isCheckFolder=new File(imgPath).mkdirs();
            String timestamp = System.currentTimeMillis()+"";
            FileOutputStream fos = new FileOutputStream(new File(imgPath+"/" + filename)); 
            fos.write(dearr); 
            fos.close();
            
            Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
            //add owners permission
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            //add group permissions
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            //add others permissions
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);
            Files.setPosixFilePermissions(Paths.get(imgPath+"/" +  filename), perms);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
   }

 
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
    
    
    // CROP IMAGE
    private CroppedImage croppedImage;
     
    private String newImageName;
 
    public CroppedImage getCroppedImage() {
        return croppedImage;
    }
 
    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }
 
    public void crop(ActionEvent evt) {
        
        if(croppedImage == null) {
            return;
        }
        
        try {
            image = (UploadedFile) croppedImage;
            selectedNews.setImageName(image.getFileName());
            imageContent = Base64.getEncoder().encodeToString(image.getContents());

            imageStreamContent = new DefaultStreamedContent(image.getInputstream(), "image/png");
        } catch (IOException ex) {
            Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    private String getRandomImageName() {
        int i = (int) (Math.random() * 100000);
         
        return String.valueOf(i);
    }
     
    public String getNewImageName() {
        return newImageName;
    }
 
    public void setNewImageName(String newImageName) {
        this.newImageName = newImageName;
    }

    public UploadedFile getVideo() {
        return video;
    }

    public void setVideo(UploadedFile video) {
        this.video = video;
    }

    public String getMediaServer() {
        return mediaServer;
    }

    public String getVideoContent() {
        return videoContent;
    }

    public void setVideoContent(String videoContent) {
        this.videoContent = videoContent;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    
    
}
