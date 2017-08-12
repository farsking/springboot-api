package com.yanbin.service.base;

import com.yanbin.model.enums.SeqType;

import java.util.List;

/**
 * Created by yanbin on 2017/7/8.
 */
public interface ISequence {

    /**
     * 获取一个新的序列, 请注意如果是执行批量插入, 则不要在你的Business Service中调用此方法,
     * <br>否则因为是同一次事务, 还未commit, 你会获取到相同的ID
     *
     * @param seqName
     * @return
     */
    Long newKey(SeqType seqType);

    /**
     * 获取一个新的一组序列
     *
     * @param seqName
     * @return
     */
    List<Long> newKeys(SeqType seqType, int size);

}
