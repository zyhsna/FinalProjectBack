package edu.zyh.finalproject.service;

import edu.zyh.finalproject.POJO.Auction;
import edu.zyh.finalproject.POJO.AuctionDetail;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zyhsna
 */
public interface AuctionService {
    /**
     * 创建新的拍卖
     *
     * @param newAuction 新拍卖
     * @return 1 创建成功
     */
    int createAuction(Auction newAuction);

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
    AuctionDetail queryAuctionDetail(int auctionId, int userId);

    /**
     * 新增竞价
     *
     * @param auctionDetail auctionDetail
     * @return 1 新增成功
     */
    int createAuctionDetail(AuctionDetail auctionDetail);

    /**
     * 更新报价
     *
     * @param auctionDetail auctionDetail
     * @return 1更新成功
     */
    int updateBid(AuctionDetail auctionDetail);

    /**
     * 选择竞标赢家
     *
     * @param auctionId 竞拍ID
     * @param bidderId  bidderId
     * @return 1 修改成功
     */
    int selectWinner(int auctionId, int bidderId);

    /**
     * 关闭拍卖
     *
     * @param auctionId 拍卖ID
     * @return 1 成功
     */
    int closeAuction(int auctionId);

    /**
     * 查询某个拍卖下的所有出价
     *
     * @param auctionId 拍卖ID
     * @return List
     */
    List<AuctionDetail> queryBidder(int auctionId);

    /**
     * 用户确认参加
     * @param auctionId 拍卖ID
     * @param userId 用户ID
     * @return 1 成功
     */
    int bidderParticipated(int auctionId,int userId);
}
