package com.gome.iam.domain.user;

/**
 * @author yintongjiang
 * @params
 * @since 2016/12/5
 */
public class LocalUserVo {
  private String sort;
  private String order;
  private String userName;
  private Integer offset;
  private Integer limit;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "LocalUserVo{" +
                "sort='" + sort + '\'' +
                ", order='" + order + '\'' +
                ", userName='" + userName + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
