Ext.namespace("com.zz91.zzwork.hr");

com.zz91.zzwork.hr.CountView = Ext.extend(Ext.form.FormPanel,{
	construtor:function(config){
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
				layout
			}];
	};
}
});
com.zz91.zzwork.staff.EntryWin=function(){
	var form = new com.zz91.zzwork.staff.StaffInfo({
		saveUrl:Context.ROOT+"/staff/createStaff.htm",
		region:"center"
	});
	
	var win = new Ext.Window({
		id:STAFF.STAFFWIN,
		title:"员工入职",
		width:700,
		autoHeight:true,
		modal:true,
		items:[form]
	});
	
	win.show();
}