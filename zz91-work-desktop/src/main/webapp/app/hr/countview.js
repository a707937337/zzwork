Ext.namespace("com.zz91.zzwork.hr");

/***
 * form表单
 */
com.zz91.zzwork.hr.CountView = Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
	config = config || {};
	Ext.apply(this,config);
	var c = {
			layout:"column",
			frame:true,
			labelAlign:'right',
			labelWidth:100,
			defaults:{
				anchor:"95%",
				xtype:"textfield"
			},
			itmes:[{
				layout:"form",
				fieldLable:"统计月份",
				xtype:"datefield",
				minValue:'12/31/2011',
				name : 'gmtWork',
				id : 'gmtWork'
			}],
	};
		com.zz91.zzwork.hr.CountView.superclass.constructor.call(this,c);
	},
	initFocus:function(){
		this.findById("gmtWork").focus(true,100);
	}
});

/****
 * 查询
 */
com.zz91.zzwork.hr.count = function(form ,dosucess){
	form.getForm.submit({
		url:Context.ROOT+"/.htm",
		method:"post",
		type:"json",
		waitMsg:"正在统计，请耐心等待！",
		//success:onSuccess,
		//failure:function(_form,_action){
			//Ext.MessageBox.show({
				//title:MESSAGE.title,
				//msg : _action.result.data,
				//buttons:Ext.MessageBox.OK
				//icon:Ext.MessageBox.ERROR
			//});
		//}
	});
};
/****
 * 重置
 */
com.zz91.zzwork.hr.result = function(countform){
	countform.form.reset();
};

com.zz91.zzwork.hr.countWin = function(doSuccess){
	var form = new com.zz91.zzwork.hr.CountView({
		region:"center"
	});
	
	var win = new Ext.Window({
		id:'sd',
		layout:'broder',
		iconCls:"lock16",
		width:300,
		height:120,
		closeable:false,
		modal:true,
		items:[form],
		keys:[{
			key:[10,13],
			fn:function(){
				com.zz91.zzwork.hr.count(form,doSuccess);
			}
		}],
		buttons:[{
			text:'马上统计',
			handler:function(btn){
				com.zz91.zzwork.hr.count(form,doSuccess);
			}
		},{
			text:'重置',
			handler:function(btn){
			com.zz91.zzwork.hr.reset();
		}
		}]

	});
	win.show();
	form.initFocus();
};



