package com.bugucloud.service.tag;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bugucloud.core.entity.Tag;
import com.bugucloud.core.vo.TagVO;

import java.util.List;

/**
 * 功能描述: 标签Service
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
public interface TagService extends IService<Tag> {

    /**
     * 查询所有标签
     */
    List<TagVO> listTags();

}
