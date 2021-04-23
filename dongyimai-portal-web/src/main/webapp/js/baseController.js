app.controller("baseController", function ($scope) {

    $scope.paginationConf = {
        currentPage: 1,//当前页
        totalItems: 10,//总记录数
        itemsPerPage: 10,//一页多少条
        perPageOptions: [5, 10, 15, 20],
        onChange: function () {
            /*重新加载数据*/
            $scope.reloadList();
        }
    };


    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);

    }

    //多选按钮
    $scope.selectId = [];
    $scope.updateSelection = function ($event, id) {

        if ($event.target.checked) {
            $scope.selectId.push(id);
        } else {
            var index = $scope.selectId.indexOf(id);
            $scope.selectId.splice(index, 1);

        }
    };

    //提取JSON 某个属性值
    $scope.getJsonValue = function (jsonString, key) {
        var value = "";
        var json = JSON.parse(jsonString);
        for (let i = 0; i < json.length; i++) {
            if (i > 0) {
                value += ",";
            }
         value += json[i][key];
        }
        return value;
    }
});

