//存放主要交互逻辑js代码
//javascript  要模块化
var seckill={
    //封装秒杀相关ajax的url
    URL:{

    },
    //验证手机号,isNaN   isNot a Number 判断是否非数字
    validatePhone: function (phone){
        if(phone && phone.length==11 &&!isNaN(phone)){
            return true;
        }else return false;
    },
    //详情页秒杀逻辑
    detail:{
        //详情页初始化
        init:function (params) {
            //手机验证和登录，计时交互
            //规划交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var startTime=params['startTime'];
            var endTime=params['endTime'];
            var seckillId=params['seckillId'];
            //验证手机号
            if (seckill.validatePhone(killPhone)) {
                //绑定手机号，控制输出
                var killPhoneModal =$('#killPhoneModal');
                //显示弹出层
                killPhoneModal.modal({
                    show:true,//显示弹出层
                    backdrop:'static',//禁止位置关闭，就是点击弹出层以外的界面关闭
                    keyboard:false//禁止用键盘关闭弹出层
                });
                $('#killPhoneBtn').click(function(){
                    var inputPhone=$('#killPhoneKey').val();
                    if(seckill.validatePhone(inputPhone)){
                        //电话写入cookie，刷新页面
                        $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                        window.location.reload();
                    }else{
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }

        }

    }
}