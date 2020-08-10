/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.data.DataPreprocessor;
import vn.mobifone.loaphuong.security.SecUser;
import vn.mobifone.loaphuong.user.User;
import vn.mobifone.loaphuong.util.SQLUtil;
import vn.mobifone.loaphuong.util.StringUtil;


/**
 *
 * @author TungLM
 */
public class ActionModel extends DataPreprocessor {

    /**
     * checkNode
     *
     * @param Id
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean checkNode(Long actionId) throws SQLException, Exception {
        try {
            open();
            String strSQL = "select id from action where parent_id =? ";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(1, actionId);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex.toString());
        } finally {
            close(mConnection, mStmt, mRs);
        }
    }

    /**
     * loadComboParent
     *
     * @return
     * @throws Exception
     */
    public List<SelectItem> loadComboParent() throws Exception {
        List<SelectItem> lsReturn = new ArrayList<SelectItem>();
        try {
            open();
            String strSQL = "select id,name from action "
                    + "     where type = 0 and id <> 1 order by id";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                lsReturn.add(new SelectItem(mRs.getString("ID"), mRs.getString("NAME")));
            }
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex.toString());
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return lsReturn;
    }

    public List<Action> getActionTree() throws Exception {
        List<Action> lsReturn = new ArrayList<>();
        try {
            open();
            String strSQL
                    = "SELECT   ROWNUM - 1 stt,\n"
                    + "         a.*,\n"
                    + "         (SELECT   DECODE(name,'root','',name)\n"
                    + "            FROM   action b\n"
                    + "           WHERE   b.id = a.parent_id)\n"
                    + "             parent_name,\n"
                    + "         DECODE (a.TYPE,\n"
                    + "                 0,\n"
                    + "                 'Nhóm chức năng',\n"
                    + "                 1,\n"
                    + "                 'Chức năng')\n"
                    + "             type_view \n"
                    + "  FROM   (    SELECT   a.id action_id,\n"
                    + "                       a.name action_name,\n"
                    + "                       a.type type,\n"
                    + "                       a.parent_id parent_id,\n"
                    + "                       a.action_url action_url,\n"
                    + "                       status\n"
                    + "                FROM   action a\n"
                    + "          CONNECT BY   PRIOR a.id = a.parent_id\n"
                    + "          START WITH   a.id = 1) a order by stt";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                Action action = new Action();
                //value.setOrder(mRs.getString("STT"));
                action.setActionId(mRs.getLong("action_id"));
                action.setActionName(mRs.getString("action_name"));
                action.setType(mRs.getString("TYPE"));
                action.setParentId(mRs.getLong("PARENT_ID"));
                action.setActionUrl(mRs.getString("ACTION_URL"));
                action.setStatus(mRs.getInt("STATUS"));
                action.setTypeView(mRs.getString("type_view"));
                action.setParentName(mRs.getString("parent_name"));
                action.setIsCheck((mRs.getLong("STATUS") == 1));
                lsReturn.add(action);
            }
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex.toString());
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return lsReturn;
    }

    public List<Action> getListAction() throws Exception {
        List<Action> lsReturn = new ArrayList<>();
        try {
            open();
            String strSQL
                    = "SELECT * FROM (\n"
                    + "SELECT  a.id action_id,\n"
                    + "        a.name action_name,\n"
                    + "        a.parent_id parent_id,a.action_url,a.type, null permit_code\n"
                    + "FROM   action a \n"
                    + "union \n"
                    + "SELECT  -1 action_id,\n"
                    + "        a.permit_name action_name,\n"
                    + "        a.action_id parent_id, null action_url,'2' type, a.permit_code \n"
                    + "FROM   action_permit a where a.is_valid =1)\n"
                    + "\n"
                    + "CONNECT BY   PRIOR action_id = parent_id\n"
                    + "START WITH   action_id = 1";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                Action action = new Action();
                //value.setOrder(mRs.getString("STT"));
                action.setActionId(mRs.getLong("action_id"));
                action.setType(mRs.getString("type"));
                action.setParentId(mRs.getLong("parent_id"));
                action.setActionName(mRs.getString("action_name"));
                action.setActionUrl(mRs.getString("action_url"));
                action.setPermitCode(mRs.getString("permit_code"));
                action.setIsCheck(false);
                lsReturn.add(action);
            }
        } catch (Exception ex) {
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex.toString());
        } finally {
            close(mConnection, mStmt, mRs);
        }
        return lsReturn;
    }

    /**
     * delete
     *
     * @param MODULE_ID
     * @return
     * @throws Exception
     */
    public String deleteAction(long actionId) throws Exception {
        String i = "true";
        try {
            open();
            mConnection.setAutoCommit(false);

            //Delete app
            String strSQL = "Select * from action where parent_id=?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(1, actionId);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                i = "false";
                return i;
            }
            strSQL = "DELETE FROM action WHERE id =?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(1, actionId);
            mStmt.execute();

            //Commit
            mConnection.commit();
            return i;
        } catch (Exception ex) {
            mConnection.rollback();
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex.toString());

        } finally {
            close(mConnection, mStmt, mRs);
        }
    }

    /**
     * add
     *
     * @param app
     * @return
     * @throws Exception
     */
    public void addAction(Action action) throws Exception {
        long strIdFromSequence;
        User a = SecUser.getUserLogged();
        try {
            open();
            mConnection.setAutoCommit(false);
            //Update app
            strIdFromSequence = SQLUtil.getSequenceValue(mConnection, "ACTION_SEQ", "action", "id");
            String strSQL
                    = " INSERT INTO ACTION(ID, NAME,"
                    + " TYPE, PARENT_ID, ACTION_URL,STATUS) VALUES(?,?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(1, strIdFromSequence);
            mStmt.setString(2, StringUtil.fix(action.getActionName()));
            mStmt.setString(3, StringUtil.fix(action.getType() + ""));
            mStmt.setLong(4, action.getParentId());
            mStmt.setString(5, StringUtil.fix(action.getActionUrl()));
            if (action.isIsCheck() == true) {
                mStmt.setInt(6, 1);
            } else {
                mStmt.setInt(6, 0);
            }
            mStmt.execute();

            String strSQL2
                    = " INSERT INTO ACTION_PERMIT(ACTION_ID, PERMIT_NAME,PERMIT_ID, IS_VALID,PERMIT_CODE )"
                    + " VALUES(?,?,?,?,?)";
            List<Permit> lis = action.getListPermit();
            if (lis != null && lis.size() > 0) {
                mStmt = mConnection.prepareStatement(strSQL2);
                for (Permit p : lis) {
                    mStmt.setLong(1, strIdFromSequence);
                    mStmt.setString(2, p.getPermitName());
                    mStmt.setLong(3, p.getPermitId());
                    mStmt.setInt(4, p.isCheck() ? 1 : 0);
                    mStmt.setString(5, p.getPermitCode()+ strIdFromSequence );
                    mStmt.execute();
                    mStmt.clearParameters();
                }
            }
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex.toString());

        } finally {
            close(mConnection, mStmt, mRs);
        }

    }

    /**
     * edit
     *
     * @param action
     * @throws Exception
     */
    public void editAction(Action action) throws Exception {
        User a = SecUser.getUserLogged();
        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            String strSQL = "UPDATE ACTION "
                    + "      SET NAME=?,TYPE=?,PARENT_ID=?,ACTION_URL=?,STATUS=?"
                    + "      Where ID=? ";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, action.getActionName());
            mStmt.setString(2, action.getType());
            mStmt.setLong(3, action.getParentId());
            mStmt.setString(4, action.getActionUrl());
            if (action.isIsCheck() == true) {
                mStmt.setInt(5, 1);
            } else {
                mStmt.setInt(5, 0);
            }
            mStmt.setLong(6, action.getActionId());
            mStmt.executeUpdate();
            close(mStmt);
            //---------------------------delete cũ---------------------------------------
            String strSQL2
                    = " DELETE FROM ACTION_PERMIT WHERE ACTION_ID = ? ";
            List<Permit> lis = action.getListPermit();
            if (lis != null && lis.size() > 0) {
                mStmt = mConnection.prepareStatement(strSQL2);
                mStmt.setLong(1, action.getActionId());
                mStmt.execute();
                mStmt.close();
            }

            //--------------------------------------------them quyen moi-----------------------
            strSQL2
                    = " INSERT INTO ACTION_PERMIT(ACTION_ID, PERMIT_NAME,PERMIT_ID, IS_VALID,PERMIT_CODE) "
                    + " VALUES(?,?,?,?,?)";
            if (lis != null && lis.size() > 0) {
                mStmt = mConnection.prepareStatement(strSQL2);
                for (Permit p : lis) {
                    mStmt.setLong(1, action.getActionId());
                    mStmt.setString(2, p.getPermitName());
                    mStmt.setLong(3, p.getPermitId());
                    mStmt.setInt(4, p.isCheck() ? 1 : 0);
                    mStmt.setString(5, p.getPermitCode());
                    mStmt.execute();
                    mStmt.clearParameters();
                }
            }

            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex.toString());

        } finally {
            close(mConnection, mStmt, mRs);
        }
    }

    public List<Permit> getPermitByActionId(long actionId) throws Exception {
        List<Permit> listPermit = new ArrayList<>();
        try {
            open();
            String strSQL
                    = " SELECT  * FROM  ACTION_PERMIT WHERE ACTION_ID =  ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(1, actionId);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                Permit permit = new Permit(mRs.getLong("permit_id"), mRs.getString("permit_name"), mRs.getInt("is_valid") == 1,mRs.getString("permit_code"));
                listPermit.add(permit);
            }
        } catch (Exception ex) {

            SystemLogger.getLogger().error(ex);
            throw new Exception(ex.toString());

        } finally {
            close(mConnection, mStmt, mRs);
        }
        return listPermit;
    }

}
