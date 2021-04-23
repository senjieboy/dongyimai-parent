//控制层
app.controller('goodsController', function ($scope, $location, $controller, goodsService, uploadService, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }


    //查询实体
    $scope.findOne = function () {
        var id = $location.search()['id'];
        if (id == null) {
            return;
        }
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
                //富文本编辑器中添加文字
                editor.html($scope.entity.goodsDesc.introduction);
                $scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages)
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
                for (var i = 0; i < $scope.entity.itemList.length; i++) {
                    $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                }

            }
        );
    };
    $scope.checkAttr = function (specName, optionName) {
        var items = $scope.entity.goodsDesc.specificationItems;
        var object = $scope.searchBayKey(items, 'attributeName', specName);
        if (object == null) {
            return false;
        } else {
            if (object.attributeValue.indexOf(optionName) >= 0) {
                return true;
            } else {
                return false;
            }

        }
    }

    //商品组合实体类
    $scope.entity = {
        goods: {},
        goodsDesc: {
            itemImages: [],
            customAttributeItems: [],
            specificationItems: []
        }
    }


    //保存
    $scope.save = function () {
        $scope.entity.goodsDesc.introduction = editor.html();
        var serviceObject;//服务层对象
        if ($scope.entity.goods.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    alert(response.message);
                    $scope.entity = {goods: {}, goodsDesc: {}}
                    editor.html('');

                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
//	Images upload
    $scope.uploadImages = function () {
        uploadService.uploadImage().success(function (response) {
            if (response.success) {
                $scope.imageEntity.url = response.message;
            } else {
                alert(response.message);
            }
        }).error(function () {
            alert("Upload fail! ");
        })
    }
    //添加图片列表
    $scope.addImages = function () {
        $scope.entity.goodsDesc.itemImages.push($scope.imageEntity);

    };

    //添加图片列表
    $scope.deleImages = function (index) {
        $scope.entity.goodsDesc.itemImages.splice(index, 1);
    }
    //读取一级分类
    $scope.selectItemCatList = function () {
        itemCatService.findByParentId(0).success(function (response) {
            $scope.itemCatList = response;

        })
    }
    //读取二级分类
    $scope.$watch("entity.goods.category1Id", function (newValue) {
        if (newValue) {
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCatList2 = response;
            })
        }
    })
    //读取三级分类
    $scope.$watch("entity.goods.category2Id", function (newValue) {
        if (newValue) {
            itemCatService.findByParentId(newValue).success(function (response) {
                $scope.itemCatList3 = response;
            })
        }
    })
    //读取模板id
    $scope.$watch("entity.goods.category3Id", function (newValue) {
        if (newValue) {
            itemCatService.findOne(newValue).success(function (response) {
                $scope.entity.goods.typeTemplateId = response.typeId;


            })
        }
    })
    //读取品牌
    $scope.$watch("entity.goods.typeTemplateId", function (newValue) {
        if (newValue) {
            typeTemplateService.findOne(newValue).success(function (response) {
                $scope.templateIds = response;
                $scope.templateIds.brandIds = JSON.parse($scope.templateIds.brandIds);
                if ($location.search()['id'] == null) {
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.templateIds.customAttributeItems);
                }

            });
            //根据模板id查询规格选项
            typeTemplateService.findSpecList(newValue).success(
                function (response) {
                    $scope.findSpecOptionList = response;

                })
        }
    });


    $scope.checkSpec = function ($event, name, value) {
        var object = $scope.searchBayKey($scope.entity.goodsDesc.specificationItems, 'attributeName', name);
        if (object != null) {
            if ($event.target.checked) {
                object.attributeValue.push(value);
            } else {
                //取消勾线
                object.attributeValue.splice(object.attributeValue.indexOf(value), 1);
                //如果勾选的选项都取消
                if (object.attributeValue.length == 0) {
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object), 1);

                }
            }
        } else {
            $scope.entity.goodsDesc.specificationItems.push({"attributeName": name, "attributeValue": [value]});
        }

    }

    $scope.createItemList = function () {
        $scope.entity.itemList = [{
            spec: {},
            price: 0,
            num: 0,
            status: '0',
            isDefault: '0'

        }];

        var items = $scope.entity.goodsDesc.specificationItems;
        for (var i = 0; i < items.length; i++) {

            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);

        }
    }

    addColumn = function (skuList, attributeName, attributeValue) {
        //创建一个新的集合
        var newList = [];
        for (let i = 0; i < skuList.length; i++) {
            var oldRow = skuList[i];
            for (var j = 0; j < attributeValue.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[attributeName] = attributeValue[j]
                newList.push(newRow);
            }
        }
        return newList;

    }

    $scope.auStatus = ['未审核', '已审核', '审核未通过', '关闭'];//0:未审核 1:已审核 2:审核通过 3:关闭
    //分类查询
    $scope.classify = [];
    $scope.findClassify = function () {
        itemCatService.findAll().success(function (response) {
            for (var i = 0; i < response.length; i++) {
                $scope.classify[response[i].id] = response[i].name;
            }
        })
    }
});	