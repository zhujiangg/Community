package com.nowcoder.community.community.entity;

/**
 * @Author: ZhuJiang
 * @Date: 2022/4/18 15:56
 * @Version: 1.0
 * @Description: 封装分页相关的信息
 *                  总行数、每页数量、当前页、路径
 *                  1、返回总页数
 *                  2、返回当前页的起始行
 *                  3、当前页的开始页
 *                  4、当前页的结束页
 *
 */
public class Page {
    private int rows;
    private int limit = 10;
    //当前页数
    private int current = 1;
    //查询路径（用于复用分页，比如帖子分页是 "/index"，评论分页是 ”/discuss/detail/“+discussPostId）
    private String path;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //返回总页数
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    //返回当前页的起始行offset
    //向上取整: 0, 10, 20……
    public int getOffset() {
        return (current - 1) * limit;
    }

    //获取起始页码
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    //获取结束页码
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }
}
