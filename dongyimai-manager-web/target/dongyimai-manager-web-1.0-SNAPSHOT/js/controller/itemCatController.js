//商品类目控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        itemCatService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            serviceObject = itemCatService.add($scope.entity);//增加
            $scope.entity.parentId = $scope.parentId;//刷新页面
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    // $scope.findByParentId($scope.parentId);
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除

    $scope.dele = function () {
        //获取选中的复选框
        itemCatService.dele($scope.selectId).success(
            function (response) {
                if (response.success) {
                    $scope.findByParentId($scope.parentId);//刷新列表
                    $scope.selectId = [];
                }
            }
        )
    };

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.parentId = 0;
    $scope.search = function (page, rows) {
        itemCatService.search($scope.parentId, page, rows,).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数

            }
        );
    }
   //修改记住id
    $scope.findByParentId = function (parentId) {
        //记住上一个id
        $scope.parentId = parentId;
        itemCatService.findByParentId(parentId).success(function (response) {
            $scope.list = response;

        })
    }

    //面包屑-关键点
    $scope.level = 1;
    $scope.setLevel = function (value) {
        $scope.level = value;
    }
    $scope.entity2 = null;
    $scope.entity3 = null;
    $scope.selectList = function (p_entity) {

        if ($scope.level == 1) {

            $scope.entity2 = null;
            $scope.entity3 = null;
        }
        if ($scope.level == 2) {
            $scope.entity2 = p_entity;
            $scope.entity3 = null;
            $scope.parentId = $scope.entity2.id;
        }
        if ($scope.level == 3) {
            $scope.entity3 = p_entity;
            $scope.parentId = $scope.entity3.id;
        }
        //2.查询下级
        $scope.search(p_entity.id, $scope.paginationConf.itemsPerPage);

    }

    //模板列表
    $scope.typeTemplateList = {data: []};
    $scope.findtypeTemplateList = function () {
        typeTemplateService.selectOptionList().success(function (response) {
            $scope.typeTemplateList = {data: response};

        })
    }

});