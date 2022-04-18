package edu.zyh.finalproject.dao;

import edu.zyh.finalproject.POJO.Auction;
import edu.zyh.finalproject.POJO.AuctionDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AuctionDao {

    /**
     * 创建新的拍卖
     *
     * @param newAuction 新拍卖
     * @return 1 创建成功
     */
    @Insert("insert into auction(task_id, auctioneer_id, bidder_num) value " +
            "(#{auction.taskId}, #{auction.auctioneerId}, #{auction.bidderNum})")
    int createAuction(@Param("auction") Auction newAuction);

    /**
     * 根据TaskId查询对应的auction信息
     *
     * @param taskId 任务Id
     * @return auction
     */
    @Select("select * from auction where task_id = #{taskId}")
    Auction queryAuctionByTaskId(int taskId);

    /**
     * 根据任务ID和用户ID查询这个用户是否有参与过竞价
     *
     * @param auctionId 拍卖ID
     * @param userId 用户ID
     * @return 如果有返回详情
     */
    @Select("select * from auction_detail where auction_id = #{auctionId} and bidder_id = #{userId}")
    AuctionDetail queryAuctionDetailByAuctionIdAndUserId(int auctionId, int userId);

    /**
     * 新增竞价
     * @param auctionDetail auctionDetail
     * @return 1 新增成功
     */
    @Insert("insert into auction_detail(bidder_id, bid, bid_time,auction_id) VALUE (#{auctionDetail.bidderId}, #{auctionDetail.bid}, #{auctionDetail.bidTime},#{auctionDetail.auctionId})")
    int createAuctionDetail(@Param("auctionDetail") AuctionDetail auctionDetail);


    /**
     * 更新报价
     * @param auctionDetail auctionDetail
     * @return 1更新成功
     */
    @Update("update auction_detail set bid = #{auctionDetail.bid},bid_time=#{auctionDetail.bidTime}where bidder_id = #{auctionDetail.bidderId} and auction_id = #{auctionDetail.auctionId}")
    int updateBid(@Param("auctionDetail") AuctionDetail auctionDetail);

    /**
     * 选择竞标赢家
     * @param auctionId 竞拍ID
     * @param bidderId bidderId
     * @return 1 修改成功
     */
    @Update("update auction_detail set selected = 1 where auction_id = #{auctionId} and bidder_id = #{bidderId}")
    int selectWinner(int auctionId,int bidderId);

    /**
     * 关闭拍卖
     * @param taskId 与拍卖关联的taskID
     * @return 1 成功
     */
    @Update("update auction set state = 1 where task_id = #{taskId}")
    int closeAuctionByTaskId(int taskId);

    /**
     * 查询某个拍卖下的所有出价
     * @param auctionId 拍卖ID
     * @return List
     */
    @Select("select * from auction_detail where auction_id = #{auctionId}")
    List<AuctionDetail> queryBidderByAuctionId(int auctionId);


    /**
     * 用户决定参与任务
     * @param auctionId 拍卖ID
     * @param bidderId 用户ID
     * @return 1 成功
     */
    @Update("update auction_detail set participated = 1 where auction_id = #{auctionId} and bidder_id = #{bidderId}")
    int bidderParticipate(int auctionId,int bidderId);

    /**
     * 查询拍卖信息
     * @param auctionId 拍卖信息
     * @return auction
     */
    @Select("select * from auction where auction_id = #{auctionId}")
    Auction queryAuctionByAuctionId(int auctionId);

    /**
     * 增加竞拍人数
     * @param auctionId 竞拍ID
     * @return 1
     */
    @Update("update auction set bidder_num = bidder_num +1")
    int increaseBidderNum(int auctionId);

    /**
     * 查询该用户是否有过出价
     * @param auctionId 拍卖ID
     * @param bidderId 出价者ID
     * @return 1 存在 0 不存在
     */
    @Select("select count(*) from auction_detail where auction_id = #{auctionId} and bidder_id = #{bidderId}")
    int queryAuctionDetailExistence(int auctionId, int bidderId);
}
