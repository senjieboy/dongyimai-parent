app.service('brandService', function ($http) {

    this.findAll = function () {
        return $http.get('../brand/findAll.do');
    };
    /**
     *
     * @param page
     * @param rows
     * @returns {*}
     */
    this.findPage = function (page, rows) {
        return $http.get('../brand/findPage.do?page=' + page + '&rows=' + rows);
    };
    /**
     *
     * @param entity
     * @returns {*}
     */
    this.update = function (entity) {
        return $http.post('../brand/update.do', entity);
    }
    /**
     *
     * @returns {*}
     */
    this.add = function (entity) {
        return $http.post('../brand/add.do', entity);
    }

    /**
     *
     * @param id
     * @returns {*}
     */
    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id=' + id);
    }
    /**
     *
     * @returns {*}
     */
    this.delete = function (ids) {
        return $http.get('../brand/delete.do?id=' + ids);
    }
    /**
     *
     * @param page
     * @param rows
     * @param serchEntity
     * @returns {*}
     */
    this.search = function (page, rows, serchEntity) {
        return $http.post('../brand/search.do?page=' + page + '&rows=' + rows, serchEntity);
    }

    this.finds = function () {
        return $http.get('../brand/findBrandList.do');
    }

})