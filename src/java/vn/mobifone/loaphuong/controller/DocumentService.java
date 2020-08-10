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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.lib.Session;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import vn.mobifone.loaphuong.entity.AreaEntity;
import vn.mobifone.loaphuong.model.AreaModel;
import vn.mobifone.loaphuong.entity.AreaTree;
import vn.mobifone.loaphuong.webservice.AreaWebservice;
 

public class DocumentService {
    private List<AreaTree> listArea;

    public List<AreaTree> getListArea() {
        return listArea;
    }

    public void setListArea(List<AreaTree> listArea) {
        this.listArea = listArea;
    }
    
    
    
     
    public AreaTree createAreaTree() {
    
    
        AreaTree areaRoot = new AreaTree();
        areaRoot.setData(areaRoot);
        
        
        
        AreaModel areaModel = new AreaModel();
        try {
            //listArea = areaModel.getListAreaByAreaId(SecUser.getUserLogged().getAreaId());
            listArea = (new AreaWebservice()).getListArea(SecUser.getUserLogged().getAreaId());
            
            for (AreaTree areaTree : listArea) {
                areaTree.setData(areaTree);
                if (areaTree.getParentAreaId() != 0 ) {
                    for (AreaTree areaTreeParent : listArea) {
                        if (areaTree.getParentAreaId()-areaTreeParent.getAreaId() == 0) {
                            areaTreeParent.getChildren().add(areaTree);
                            break;
                        }
                    }
                }
            }
            
//            for (AreaTree areaTree : listArea) {
//                if (areaTree.getParentAreaId() == 0) {
//                    areaRoot.getChildren().add(areaTree);
//                    return areaRoot;
//                }
//            }
            
            areaRoot.getChildren().add(listArea.get(0));
            
            
            
            
        } catch (Exception ex) {
            Logger.getLogger(DocumentService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    
        
        return areaRoot;
    }
    
    
    
}