app.controller('searchController', function ($scope, $location, searchService) {


    $scope.search = function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);
        searchService.search($scope.searchMap).success(function (response) {

            $scope.resultMap = response;
            buildPageTag();
        })
    }
    $scope.searchMap = {
        'keywords': '',
        'category': '',
        'brand': '',
        'price': '',
        'pageNo': 1,
        'pageSize': 10,
        'sortField': '',
        'sort': '',
        'updatetime':'',
        'spec': {}

    }
    //添加搜索项
    $scope.addSearchMap = function (key, value) {
        if (key === 'category' || key === 'brand' || key === 'price') {
            $scope.searchMap[key] = value;

        } else {
            $scope.searchMap.spec[key] = value;
        }

        $scope.search();
    }
    $scope.deleteSearch = function (key) {
        if (key === 'category' || key === 'brand' || key === 'price') {
            $scope.searchMap[key] = '';

        } else {
            delete $scope.searchMap.spec[key];
        }
    }
    //价格过滤
    $scope.keywordsIsBrand = function () {
        for (let i = 0; i < $scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0) {
                return true;
            }
        }
        return false;
    }
    //分页
    buildPageTag = function () {
        //分页栏数组
        $scope.pageTag = [];
        //总页数
        let maxPage = $scope.resultMap.totalPages;
        //开始页码
        let firstPage = 1;
        $scope.firstPoint = true;
        $scope.lastPoint = true;

        //截至页码
        let lastPage = maxPage;
        //总页数大于5页、
        if ($scope.resultMap.totalPages > 5) {
            if ($scope.searchMap.pageNo <= 3) {
                lastPage = 5;
                $scope.firstPoint = false;
            } else if ($scope.searchMap.pageNo >= lastPage - 2) {
                firstPage = maxPage - 4;
                $scope.lastPoint = false;
            } else {
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
            }
        } else {
            $scope.firstPoint = false;
            $scope.lastPoint = false;
        }
        //循环产生页签
        for (let i = firstPage; i <= lastPage; i++) {
            $scope.pageTag.push(i);
        }
    }
    //页码验证
    $scope.queryByPage = function (pageNo) {
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    }
    //如果当夜页码为第一页
    $scope.isFirstPage = function () {
        switch ($scope.searchMap.pageNo) {
            case 1:
                return true;
            default:
                return false;
        }

    }
    //设置排序规则
    $scope.sortSearch = function (sortFile, sort) {
        $scope.searchMap.sortField = sortFile;
        $scope.searchMap.sort = sort;
        $scope.search();
    }
    $scope.loadKeywords = function () {
        $scope.searchMap.keywords = $location.search()['keywords'];
        $scope.search();
    }
})