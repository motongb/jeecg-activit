package org.jeecg.common.util;

import org.jeecg.modules.base.entity.TreeEntity;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author motb
 * @date 2021/2/2 8:57
 * @description //TODO TreeBuilder
 **/
public final class TreeBuilder {
    private TreeBuilder() {
    }

    public static <T extends TreeEntity> List<T> build(List<T> treeNodes, String root) {
        List<T> trees = new ArrayList<>();

        for (T treeNode : treeNodes) {
            for (T it : treeNodes) {
                if (treeNode.getId().equals(it.getPid())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
            if (StringUtils.isEmpty(treeNode.getPid()) || root.equals(treeNode.getPid())) {
                trees.add(treeNode);
            }
        }
        return trees;
    }
}
