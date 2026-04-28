package com.bugucloud.service.tag.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.Tag;
import com.bugucloud.core.mapper.TagMapper;
import com.bugucloud.service.tag.TagService;
import org.springframework.stereotype.Service;

/**
 * 功能描述: 标签Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
