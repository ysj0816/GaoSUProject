mui.plusReady(function() {
    mui.init({
        swipeBack: true //启用右滑关闭功能
    });
    mui.ajax('http://' + window.localStorage.getItem("ip") + ':' + window.localStorage.getItem("port") + '/Sys_Department/GetDept', {
        data: {},
        dataType: 'json',
        type: 'post',
        timeout: 10000, //超时时间设置为10秒
        success: function(data) {

            alert(data[0].DeptName);
        },
        error: function(xhr, type, errorThrown) {
            //异常处理；
            alert(type);
        }
    });

    var serArea = [{
        "DeptId": "e0619a20-7d33-4d48-874f-45c0d9314f5e",
        "DeptName": "汉十孝感服务区",
        "Descript": null,
        "Category": "3",
        "Address": "孝感",
        "ParentDept": "203a6b63-316e-4d02-9604-4673b5362ab2",
        "Sort": null,
        "Remark": null,
        "Longitude": 113.982414,
        "Latitude": 31.0139046,
        "DeptCode": "010301"
    }, {
        "DeptId": "e44ee4ae-87b7-42d9-8068-52d029fd4656",
        "DeptName": "京珠高速孝感服务区",
        "Descript": "京珠高速孝感服务区",
        "Category": "3",
        "Address": "孝感",
        "ParentDept": "203a6b63-316e-4d02-9604-4673b5362ab2",
        "Sort": null,
        "Remark": null,
        "Longitude": 114.074463,
        "Latitude": 31.0092373,
        "DeptCode": "010303"
    }, {
        "DeptId": "1b3cc300-2b1f-451b-942d-70c217bcf4cc",
        "DeptName": "东西湖服务区",
        "Descript": "东西湖服务区",
        "Category": "3",
        "Address": "东西湖",
        "ParentDept": "203a6b63-316e-4d02-9604-4673b5362ab2",
        "Sort": null,
        "Remark": null,
        "Longitude": 114.047554,
        "Latitude": 30.7069359,
        "DeptCode": "010306"
    }, {
        "DeptId": "9cf99de4-97da-4147-8dd1-72f0464d1e35",
        "DeptName": "安陆服务区",
        "Descript": null,
        "Category": "3",
        "Address": "安陆",
        "ParentDept": "203a6b63-316e-4d02-9604-4673b5362ab2",
        "Sort": null,
        "Remark": null,
        "Longitude": 113.546043,
        "Latitude": 31.3648853,
        "DeptCode": "010302"
    }, {
        "DeptId": "98e45eaf-37e3-4eb4-beca-81564be314ab",
        "DeptName": "京山服务区",
        "Descript": "京山服务区",
        "Category": "3",
        "Address": "京山",
        "ParentDept": "203a6b63-316e-4d02-9604-4673b5362ab2",
        "Sort": null,
        "Remark": null,
        "Longitude": 113.24707,
        "Latitude": 31.2272282,
        "DeptCode": "010305"
    }, {
        "DeptId": "2a675392-0790-4bb2-b30d-b24cc8fc179d",
        "DeptName": "天门服务区",
        "Descript": "天门服务区",
        "Category": "3",
        "Address": "天门",
        "ParentDept": "203a6b63-316e-4d02-9604-4673b5362ab2",
        "Sort": null,
        "Remark": null,
        "Longitude": 113.1246,
        "Latitude": 30.6736145,
        "DeptCode": "010304"
    }]

});
