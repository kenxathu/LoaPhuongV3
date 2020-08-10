/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.role;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vn.mobifone.loaphuong.lib.SystemLogger;
import vn.mobifone.loaphuong.lib.data.DataPreprocessor;
import vn.mobifone.loaphuong.util.SQLUtil;
import vn.mobifone.loaphuong.action.Action;

/**
 *
 * @author tho.nt
 */
public class RoleModel extends DataPreprocessor {
    // Lay danh sach vai tro
    public List<Role> getListRole() throws SQLException, Exception {
        List<Role> returnValue = new ArrayList<>();
        try {
            open();
            mConnection.setAutoCommit(false);
            String sql = "select  id, name from role";
            mStmt = mConnection.prepareStatement(sql);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                Role role = new Role(mRs.getLong("id"), mRs.getString("name"));
                returnValue.add(role);
            }

        } catch (Exception ex) {
            mConnection.rollback();
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex);

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }
        return returnValue;
    }
    public List<Long> getListActionSelected(long roleId) throws SQLException, Exception {
        List<Long> returnValue = new ArrayList<>();
        try {
            open();
            String strSQL = ""
                    + "SELECT a.ACTION_URL , a.ID, a.NAME, b.PERMIT_CODE"
                    + "  FROM ACTION a, ROLE_ACTION b" 
                    + "  WHERE a.ID = b.ACTION_ID and b.ROLE_ID = ?";

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(1, roleId);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                returnValue.add(mRs.getLong("ID"));
            }

        } catch (Exception ex) {
            
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex);

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }
        return returnValue;
    }
    public List<Action> getListActionByRoleId(long roleId) throws SQLException, Exception {
        List<Action> returnValue = new ArrayList<>();
        try {
            open();
            String strSQL = ""
                    + "  SELECT a.ACTION_URL , a.ID, a.NAME, b.PERMIT_CODE,a.type,a.parent_id "
                    + "  FROM ACTION a, ROLE_ACTION b" 
                    + "  WHERE a.ID = b.ACTION_ID and b.ROLE_ID = ?";

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(1, roleId);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                Action action = new Action();
                action.setActionId(mRs.getLong("ID"));
                action.setParentId(mRs.getLong("parent_id"));
                action.setType(mRs.getString("type"));
                action.setActionName(mRs.getString("NAME"));
                action.setActionUrl(mRs.getString("ACTION_URL"));
                action.setPermitCode(mRs.getString("PERMIT_CODE"));
                returnValue.add(action);
            }

        } catch (Exception ex) {
            
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex);

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }
        return returnValue;
    }

    public List<Role> addRole(Role mrole, List<Action> actions) throws SQLException, Exception {
        List<Role> returnValue = new ArrayList<>();
        try {
            open();
            mConnection.setAutoCommit(false);
            long strIdFromSequence = SQLUtil.getSequenceValue(mConnection, "ROLE_SEQ", "ROLE", "ID");
            String sql = "insert into role(id, name) values(?,?)";
            mStmt = mConnection.prepareStatement(sql);
            mStmt.setLong(1, strIdFromSequence);
            mStmt.setString(2, mrole.getRoleName());
            mStmt.execute();
            mStmt.close();
            // ---------------------------------- 
            String sqlDelete = "delete from role_action where role_id = ? ";
            mStmt = mConnection.prepareStatement(sqlDelete);
            mStmt.setLong(1, strIdFromSequence);
            mStmt.execute();
            mStmt.close();
            // Get permit in form
            Map<Long, String> permit = new HashMap<>();
            for (Action action : actions) {
                //if(action.getPermitCode() != null) continue;
                if(action.getType().equals("1") && !permit.containsKey(action.getParentId())) 
                    permit.put(action.getParentId(), "");
                else if (permit.containsKey(action.getParentId())) {
                    String newPermit = permit.get(action.getParentId()) + ";" + action.getPermitCode() + ";";
                    permit.put(action.getParentId(), newPermit);
                } else {
                    String newPermit = action.getPermitCode() + ";";
                    permit.put(action.getParentId(), newPermit);
                }
            }
            //---------------------------------------------them quyen mới
            String sqlAddnew = "insert into role_action (role_id, action_id, permit_code) values(?,?,?) ";
            mStmt = mConnection.prepareStatement(sqlAddnew);
            for (Map.Entry<Long, String> entry : permit.entrySet()) {
                mStmt.setLong(1, strIdFromSequence);
                mStmt.setLong(2, entry.getKey());
                mStmt.setString(3, entry.getValue());
                mStmt.execute();
            }
            mStmt.close();
            mConnection.commit();
        } catch (Exception ex) {
            mConnection.rollback();
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex);

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }
        return returnValue;
    }

    public List<Role> updateRole(Role mrole, List<Action> actions) throws SQLException, Exception {
        List<Role> returnValue = new ArrayList<>();
        try {
            open();
            mConnection.setAutoCommit(false);

            String sql = "update role set name = ? where id = ? ";
            mStmt = mConnection.prepareStatement(sql);
            mStmt.setString(1, mrole.getRoleName());
            mStmt.setLong(2, mrole.getRoleId());
            mStmt.execute();
            mStmt.close();

            // ---------------------------------- 
            String sqlDelete = "delete from role_action where role_id = ? ";
            mStmt = mConnection.prepareStatement(sqlDelete);
            mStmt.setLong(1, mrole.getRoleId());
            mStmt.execute();
            mStmt.close();
            // Get permit in form 
            Map<Long, String> permit = new HashMap<>();
            for (Action action : actions) {
                //if(action.getPermitCode() == null) continue;
                if(action.getType().equals("1") && !permit.containsKey(action.getParentId())) 
                    permit.put(action.getParentId(), "");
                else if (permit.containsKey(action.getParentId())) {
                    String newPermit = permit.get(action.getParentId())  + action.getPermitCode() + ";";
                    permit.put(action.getParentId(), newPermit);
                } else {
                    String newPermit = action.getPermitCode() + ";";
                    permit.put(action.getParentId(), newPermit);
                }
            }
            //---------------------------------------------them quyen mới
            String sqlAddnew = "insert into role_action (role_id, action_id, permit_code) values(?,?,?) ";
            mStmt = mConnection.prepareStatement(sqlAddnew);
            for (Map.Entry<Long, String> entry : permit.entrySet()) {
                mStmt.setLong(1, mrole.getRoleId());
                mStmt.setLong(2, entry.getKey());
                mStmt.setString(3, entry.getValue());
                mStmt.execute();
                mStmt.clearParameters();
            }
            mStmt.close();
            mConnection.commit();
        } catch (Exception ex) {
            mConnection.rollback();
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex);

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }
        return returnValue;
    }
    
    public List<Role> deleteRole(Role mrole) throws SQLException, Exception {
        List<Role> returnValue = new ArrayList<Role>();
        try {
            open();
            mConnection.setAutoCommit(false);
            // ---------------------------------- 
            String sqlDelete = "delete from role_action where role_id = ? ";
            mStmt = mConnection.prepareStatement(sqlDelete);
            mStmt.setLong(1, mrole.getRoleId());
            mStmt.execute();
            mStmt.close();
            //----------------------------------------------
            String sql = "delete from role  where id = ? ";
            mStmt = mConnection.prepareStatement(sql);
            mStmt.setLong(2, mrole.getRoleId());
            mStmt.execute();
            mStmt.close();

            mConnection.commit();
        } catch (Exception ex) {
            mConnection.rollback();
            SystemLogger.getLogger().error(ex);
            throw new Exception(ex);

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }
        return returnValue;
    }
}
