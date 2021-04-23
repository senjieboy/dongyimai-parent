app.controller('itemController', function ($scope) {
    $scope.num = 1;
    //数量操作
    $scope.addNum = function (num) {
        $scope.num = $scope.num + num;
        if ($scope.num < 1) {
            $scope.num = 1;
        }
    }
    //存储规格选中的选项
    $scope.specificationItems = {};
    $scope.selectSpecification = function (name, value) {
        $scope.specificationItems[name] = value;
        searchSku();
    }
    //判断规格选项是否被选中
    $scope.isSelected = function (name, value) {
        return $scope.specificationItems[name] === value ? true : false;

    }

    //加载默认的sku
    $scope.loadSku = function () {
        $scope.sku = skuList[0];
        $scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));
    }
    //匹配两个对象
    matchObject = function (map1, map2) {
        for (let k in map1) {
            if (map1[k] != map2[k]) {
                return false;
            }
        }
        for (let k in map2) {
            if (map2[k] != map1[k]) {
                return false;
            }
        }
        return true;
    }
    //查询SKU
    searchSku = function () {
        for (let i = 0; i < skuList.length; i++) {
            if (matchObject(skuList[i].spec, $scope.specificationItems)) {
                $scope.sku = skuList[i];
                return;
            }

        }
    }
    //
    $scope.addToCat = function () {
        alert('skuid:' + $scope.sku.id);
    }
    $scope.sku = {id: 0, title: '--------', price: 0};
})