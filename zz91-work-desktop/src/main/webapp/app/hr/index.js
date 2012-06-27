Ext.namespace('com.zz91.zzwork.hr');

com.zz91.zzwork.hr.AttendanceInForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
	config = config || {};
	Ext.apply(this,config);
	var c = {
				layout:"form",
				columnWidth:.5,
				defaults:{
					anchor:"100%",
					xtype:"datefield"
	},
			items:[{
				allowBlank:false,
				fieldLabel:"考勤时间",
				name:"gmtwork",
				id:"gmtwork"
			},{
				allowBlank:false,
				fieldLable:"登记号码",
				xtype:"textfield",
				name:"code",
				id:"code"
			},{
				allowBlank:false,
				fieldLable:"登记号码",
				name:"name",
				id:"name",
				xtype:"textfield"
			}]
	};
			com.zz91.zzwork.hr.AttendanceInForm.superclass.constructor.call(this,c);
	},
	initFocus:function(){
		this.findById("name").focus(true,true);
	}
})