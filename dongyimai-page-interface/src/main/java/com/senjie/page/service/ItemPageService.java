package com.senjie.page.service;

/**
 * @Author SenJie
 * @Data 2021/4/18 20:13
 */
public interface ItemPageService {
    /**
     * Create static pages
     *
     * @param goodId
     * @return
     */
    public boolean genItemHtml (Long goodId);

    /**
     * Delete static pages
     *
     * @param goodIds
     * @return
     */
    public boolean deleteItemHtml (Long[] goodIds);
}
