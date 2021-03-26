package org.jeecg.modules.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.TreeBuilder;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.entity.CmsComment;
import org.jeecg.modules.base.service.ICmsCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 评论表
 * @Author: jeecg-boot
 * @Date: 2021-03-18
 * @Version: V1.0
 */
@Api(tags = "评论表")
@RestController
@RequestMapping("/base/cmsComment")
@Slf4j
public class CmsCommentController extends JeecgController<CmsComment, ICmsCommentService> {

    @Autowired
    private ICmsCommentService cmsCommentService;

    /**
     * 树结构
     *
     * @return
     */
    @AutoLog(value = "评论表-树结构")
    @ApiOperation(value = "评论表-树结构", notes = "评论表-树结构")
    @GetMapping(value = "/tree")
    public Result<?> tree(CmsComment cmsComment) {
        List<CmsComment> cmsComments = cmsCommentService.listVo(cmsComment);
        return Result.OK(TreeBuilder.build(cmsComments, "0"));
    }

    /**
     * 分页列表查询
     *
     * @param cmsComment
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "评论表-分页列表查询")
    @ApiOperation(value = "评论表-分页列表查询", notes = "评论表-分页列表查询")
    @GetMapping(value = "/rootList")
    public Result<?> queryPageList(CmsComment cmsComment,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String hasQuery = req.getParameter("hasQuery");
        if (hasQuery != null && "true".equals(hasQuery)) {
            QueryWrapper<CmsComment> queryWrapper = QueryGenerator.initQueryWrapper(cmsComment, req.getParameterMap());
            List<CmsComment> list = cmsCommentService.queryTreeListNoPage(queryWrapper);
            IPage<CmsComment> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } else {
            String parentId = cmsComment.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            cmsComment.setPid(null);
            QueryWrapper<CmsComment> queryWrapper = QueryGenerator.initQueryWrapper(cmsComment, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<CmsComment> page = new Page<CmsComment>(pageNo, pageSize);
            IPage<CmsComment> pageList = cmsCommentService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
    }

    /**
     * 获取子数据
     *
     * @param cmsComment
     * @param req
     * @return
     */
    @AutoLog(value = "评论表-获取子数据")
    @ApiOperation(value = "评论表-获取子数据", notes = "评论表-获取子数据")
    @GetMapping(value = "/childList")
    public Result<?> queryPageList(CmsComment cmsComment, HttpServletRequest req) {
        QueryWrapper<CmsComment> queryWrapper = QueryGenerator.initQueryWrapper(cmsComment, req.getParameterMap());
        List<CmsComment> list = cmsCommentService.list(queryWrapper);
        IPage<CmsComment> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
        return Result.OK(pageList);
    }

    /**
     * 批量查询子节点
     *
     * @param parentIds 父ID（多个采用半角逗号分割）
     * @param parentIds
     * @return
     */
    @AutoLog(value = "评论表-批量获取子数据")
    @ApiOperation(value = "评论表-批量获取子数据", notes = "评论表-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<CmsComment> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<CmsComment> list = cmsCommentService.list(queryWrapper);
            IPage<CmsComment> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }

    /**
     * 添加
     *
     * @param cmsComment
     * @return
     */
    @AutoLog(value = "评论表-添加")
    @ApiOperation(value = "评论表-添加", notes = "评论表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody CmsComment cmsComment) {
        cmsCommentService.addCmsComment(cmsComment);

        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param cmsComment
     * @return
     */
    @AutoLog(value = "评论表-编辑")
    @ApiOperation(value = "评论表-编辑", notes = "评论表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody CmsComment cmsComment) {
        cmsCommentService.updateCmsComment(cmsComment);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "评论表-通过id删除")
    @ApiOperation(value = "评论表-通过id删除", notes = "评论表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        cmsCommentService.deleteCmsComment(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "评论表-批量删除")
    @ApiOperation(value = "评论表-批量删除", notes = "评论表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.cmsCommentService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "评论表-通过id查询")
    @ApiOperation(value = "评论表-通过id查询", notes = "评论表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CmsComment cmsComment = cmsCommentService.getById(id);
        if (cmsComment == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(cmsComment);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param cmsComment
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CmsComment cmsComment) {
        return super.exportXls(request, cmsComment, CmsComment.class, "评论表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CmsComment.class);
    }

}
