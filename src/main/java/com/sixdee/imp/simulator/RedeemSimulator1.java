package com.sixdee.imp.simulator;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;

public class RedeemSimulator1
{
  DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  Logger log = Logger.getLogger(RedeemSimulator1.class);

  String number = null;

  public RedeemSimulator1(String number) { this.number = number;
  }

  @SuppressWarnings("unchecked")
public void start()
  {
    List allLoyalty = getAllLoyaltyId();
   // List allCorportate = getAllCorporateIds();

    //allLoyalty.removeAll(allCorportate);
    findingOpeningBalance(allLoyalty);
  }

  @SuppressWarnings("unchecked")
private List<BigDecimal> getAllLoyaltyId()
  {
    Session session = null;
    Query query = null;
    List<BigDecimal> list = null;
    try
    {
      session = HiberanteUtil.getSessionFactory().openSession();
      String sql = "SELECT LOYALTY_ID FROM LOYALTY_PROFILE_0";
      if (this.number != null)
        sql = sql + " where LOYALTY_ID=" + this.number;
      query = session.createSQLQuery(sql);

      list = query.list();
      this.log.info("Got All Loyalty Ids....");
    }
    catch (Exception e)
    {
      this.log.error("Exception ", e);
    }
    finally
    {
      if (session != null) {
        session.close();
      }
      query = null;
    }

    return list;
  }

  @SuppressWarnings("unchecked")
private List<BigDecimal> getAllCorporateIds()
  {
    Session session = null;
    Query query = null;
    List<BigDecimal> list = null;
    try
    {
      session = HiberanteUtil.getSessionFactory().openSession();
      String sql = "SELECT DISTINCT LOYALTY_ID FROM CORPORATE_LINES";

      query = session.createSQLQuery(sql);

      list = query.list();
      this.log.info("Got All corporate Ids....");
    }
    catch (Exception e)
    {
      this.log.error("Exception ", e);
    }
    finally
    {
      if (session != null) {
        session.close();
      }
      query = null;
    }

    return list;
  }

  @SuppressWarnings("unchecked")
public Map<Long, Long> findingOpeningBalance(List<BigDecimal> loayaltyIds)
  {
    Session session = null;
    Query query = null;
    Map loaltyOpeningMap = new HashMap();
    TableInfoDAO tableInfoDAO = new TableInfoDAO();
    List list = null;
    try
    {
      Calendar cal = Calendar.getInstance();
      cal.set(1, Integer.parseInt((String)Cache.getCacheMap().get("YEAR")));
      cal.set(2, Integer.parseInt((String)Cache.getCacheMap().get("MONTH")) - 1);
      cal.set(5, Integer.parseInt((String)Cache.getCacheMap().get("START_DAY")));
      cal.set(11, 0);
      cal.set(12, 0);
      cal.set(13, 0);

      Calendar cal1 = Calendar.getInstance();
      cal1.set(1, Integer.parseInt((String)Cache.getCacheMap().get("YEAR")));
      cal1.set(2, Integer.parseInt((String)Cache.getCacheMap().get("MONTH")) - 1);
      cal1.set(5, Integer.parseInt((String)Cache.getCacheMap().get("END_DAY")));
      cal1.set(11, 23);
      cal1.set(12, 59);
      cal1.set(13, 59);

      for (ListIterator it = loayaltyIds.listIterator(); it.hasNext(); )
      {
        try
        {
          session = HiberanteUtil.getSessionFactory().openSession();
          long key = ((BigDecimal)it.next()).longValue();

          this.log.info("cal = " + cal.getTime() + "AND cal1 = " + cal1.getTime());

          int mnthIndx = cal.get(2) + 1;
          System.out.println(mnthIndx);

          query = session.createQuery("FROM " + tableInfoDAO.getLoyaltyTransactionTable(new StringBuilder(String.valueOf(key)).toString()) + 
            " WHERE monthIndex=" + mnthIndx + " and loyaltyID=? and createTime>=? and createTime<=? AND statusID IN (5,8) " + 
            " ORDER BY createTime,preRewardPoints");

          query.setParameter(0, Long.valueOf(key));
          query.setParameter(1, cal.getTime());
          query.setParameter(2, cal1.getTime());

          list = query.list();
          if ((list != null) && (list.size() > 0)) {
            findGenuineRedeem(session, list, false, cal.getTime());
          }

          if ((list == null) || (list.size() == 0))
          {
            int i = 1;
            String indexString = "";
            while (true)
            {
              if (mnthIndx == 1)
                mnthIndx = 12;
              else
                mnthIndx--;
              indexString = indexString + mnthIndx;
              i++;
              if (i > 9)
                break;
              indexString = indexString + ",";
            }
            query = session.createQuery("FROM " + tableInfoDAO.getLoyaltyTransactionTable(new StringBuilder(String.valueOf(key)).toString()) + 
              " WHERE monthIndex in (" + indexString + ") and LOYALTY_ID=? and createTime<=? AND STATUS_ID IN (5,8)" + 
              " ORDER BY CREATE_DATE DESC,curRewardPoints DESC");

            query.setParameter(0, Long.valueOf(key));
            query.setParameter(1, cal.getTime());
            query.setMaxResults(1);
            list = query.list();
            if ((list != null) && (list.size() > 0)) {
              findGenuineRedeem(session, list, true, cal.getTime());
            }

          }

          this.log.info("Processing DONE for loyalty id = " + key);
        }
        catch (Exception e) {
          this.log.error("Exception Please check == ==!!!", e);
        } finally {
          if (session != null)
            session.close();
          session = null;
          query = null;
        }
      }
    }
    catch (Exception e)
    {
      this.log.error("Exception while finding opening balance", e);
    }
    finally
    {
      if (session != null) {
        session.close();
      }
      query = null;
      tableInfoDAO = null;
    }

    return loaltyOpeningMap;
  }

  @SuppressWarnings("unchecked")
private void findGenuineRedeem(Session session, List<LoyaltyTransactionTabDTO> list, boolean flag, Date cdrDate)
  {
    TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
    Map cdrMap = new HashMap();
    Long loyaltyId = ((LoyaltyTransactionTabDTO)list.get(0)).getLoyaltyID();

    cdrMap.put(Integer.valueOf(3), Double.valueOf(0.0D));
    cdrMap.put(Integer.valueOf(5), Double.valueOf(0.0D));
    cdrMap.put(Integer.valueOf(7), Double.valueOf(0.0D));
    if (flag)
    {
      cdrMap.put(Integer.valueOf(2), ((LoyaltyTransactionTabDTO)list.get(0)).getCurRewardPoints());
      cdrMap.put(Integer.valueOf(4), ((LoyaltyTransactionTabDTO)list.get(0)).getCurRewardPoints());
      cdrMap.put(Integer.valueOf(5), ((LoyaltyTransactionTabDTO)list.get(0)).getCurStatusPoints());
    }
    else
    {
      cdrMap.put(Integer.valueOf(2), ((LoyaltyTransactionTabDTO)list.get(0)).getPreRewardPoints());

      Double rewardPoints = Double.valueOf(0.0D);
      Double redeemPoints = Double.valueOf(0.0D);
      Double statusbalance = ((LoyaltyTransactionTabDTO)list.get(0)).getPreStatusPoints();
      LoyaltyTransactionTabDTO dto = null;
      for (int i = 0; i < list.size(); i++)
      {
        dto = (LoyaltyTransactionTabDTO)list.get(i);

        statusbalance = Double.valueOf(statusbalance.doubleValue() + dto.getStatusPoints().doubleValue());
        if (dto.getStatusID().intValue() == 5)
        {
          rewardPoints = Double.valueOf(rewardPoints.doubleValue() + dto.getRewardPoints().doubleValue());
        }
        else if (dto.getStatusID().intValue() == 8) {
          redeemPoints = Double.valueOf(redeemPoints.doubleValue() + dto.getRewardPoints().doubleValue());
        }

      }

      cdrMap.put(Integer.valueOf(3), redeemPoints);
      cdrMap.put(Integer.valueOf(4), ((LoyaltyTransactionTabDTO)list.get(list.size() - 1)).getCurRewardPoints());
      cdrMap.put(Integer.valueOf(5), statusbalance);
      cdrMap.put(Integer.valueOf(7), rewardPoints);
    }

    LoyaltyProfileTabDTO profileTabDTO = tableDetailsDAO.getLoyaltyProfile(loyaltyId);

    cdrMap.put(Integer.valueOf(6), Double.valueOf(profileTabDTO.getTierId().doubleValue()));

    this.log.info(cdrMap);
    insertReport(session, cdrMap, loyaltyId, cdrDate);
  }

  private void insertReport(Session session, Map<Integer, Double> cdrMap, Long loayltyID, Date cdrDate)
  {
    Query query = null;
    try
    {
      session.beginTransaction();
      query = session.createSQLQuery("INSERT INTO LOYALTY_REPORT2_NEW(LOYALTY_ID,OPEN_BAL, CLOSE_BAL,REDEEMED_POINTS,STATUS_POINTS,TIER_ID,CDR_DATE,REWARD_POINTS) VALUES(?,CAST(? AS INT),CAST(? AS INT),?,?,?,?,CAST(? AS INT))");
      query.setParameter(0, loayltyID);
      query.setParameter(1, cdrMap.get(Integer.valueOf(2)));
      query.setParameter(2, cdrMap.get(Integer.valueOf(4)));
      query.setParameter(3, cdrMap.get(Integer.valueOf(3)));
      query.setParameter(4, cdrMap.get(Integer.valueOf(5)));
      query.setParameter(5, Integer.valueOf(((Double)cdrMap.get(Integer.valueOf(6))).intValue()));
      query.setParameter(6, cdrDate);
      query.setParameter(7, cdrMap.get(Integer.valueOf(7)));

      int i = query.executeUpdate();
      session.getTransaction().commit();
      this.log.info("Report inserted for loyaltyid = " + cdrMap.get(Integer.valueOf(1)) + " is =" + i);
    }
    catch (Exception e)
    {
      this.log.info("Exception while inserting report", e);
    }
    finally
    {
      query = null;
    }
  }
}