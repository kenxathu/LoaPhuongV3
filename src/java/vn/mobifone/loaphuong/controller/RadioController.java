/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.controller;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import vn.mobifone.loaphuong.entity.RadioChannel;
import vn.mobifone.loaphuong.entity.Servey;
import vn.mobifone.loaphuong.lib.ClientMessage;
import vn.mobifone.loaphuong.lib.TSPermission;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.webservice.RadioWebservice;





/**
 *
 * @author cuong.trinh
 */
@ManagedBean(name = "RadioController")
@ViewScoped
public class RadioController extends TSPermission implements Serializable  {

    //Servey
    private List<RadioChannel> listChanel;
    private RadioWebservice radioWS;
    private RadioChannel selectedChannel;
    private PersistAction channelAction;

    
    
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
    


    public RadioController() {
        radioWS = new RadioWebservice();
        loadChannel();
        channelAction = PersistAction.SELECT;
        
    }
 
 

    public void loadChannel() {
        try {
             listChanel = radioWS.getListChannel();
            
        } catch (Exception ex) {
                Logger.getLogger(RadioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void addChannelEvent(ActionEvent evt) { 
        selectedChannel = new RadioChannel();
        channelAction = PersistAction.CREATE;
    }
    
    public void editChannelEvent(RadioChannel channel) { 
        selectedChannel = channel;
        channelAction = PersistAction.UPDATE;
    }
    
    public void deleteChannelEvent(RadioChannel channel) { 
        selectedChannel = channel;
        channelAction = PersistAction.DELETE;
    }
    
    public void saveChannel(ActionEvent evt) { 
        try {
            switch(channelAction){
            case CREATE:
                radioWS.executeChannel(selectedChannel, 1);
                
                
                break;
            case UPDATE:
                radioWS.executeChannel(selectedChannel, 2);
                
                
                break;
            case DELETE:
                radioWS.deleteChannel(selectedChannel.getId());
                
                
                
                break;
            }

            loadChannel();
            
        } catch (Exception e) {
            ClientMessage.logErr("Không thành công: đã xảy ra lỗi không xác định!");
            return;
        }
        
    }
    

    public List<RadioChannel> getListChanel() {
        return listChanel;
    }

    public void setListChanel(List<RadioChannel> listChanel) {
        this.listChanel = listChanel;
    }

    public RadioChannel getSelectedChannel() {
        return selectedChannel;
    }

    public void setSelectedChannel(RadioChannel selectedChannel) {
        this.selectedChannel = selectedChannel;
    }

    public PersistAction getChannelAction() {
        return channelAction;
    }

    public void setChannelAction(PersistAction channelAction) {
        this.channelAction = channelAction;
    }
    
    
    
    
    
    
 
}
