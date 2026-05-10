package com.bugucloud.service.tag.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugucloud.core.entity.Tag;
import com.bugucloud.core.mapper.TagMapper;
import com.bugucloud.core.vo.TagVO;
import com.bugucloud.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述: 标签Service实现
 *
 * @author achen
 * @version 1.0.0
 * @date 2026/4/16
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final TagMapper tagMapper;

    @Override
    public List<TagVO> listTags() {
        // 查询所有标签
        List<Tag> tags = tagMapper.selectList(null);

        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }

        // 转换为DTO
        return tags.stream()
                .map(tag -> {
                    TagVO tagVO = new TagVO();
                    tagVO.setId(tag.getId());
                    tagVO.setName(tag.getName());
                    return tagVO;
                })
                .collect(Collectors.toList());
    }

}
