Ext.namespace('com.zz91.zzwork.hr');
com.zz91.zzwork.hr.DtoFields=[
	{name:"id",mapping:"id"},
	{name:"name",mapping:"name"},
	{name:"code",mapping:"code"},
	{name:"account",mapping:"account"},
	{name:"gmt_work",mapping:"gmtWork"},
	{name:"gmt_created",mapping:"gmtCreated"},
	{name:"gmt_modeified",mapping:"gmtModeified"}
	];

com.zz91.zzwork.hr.AttendanceTable = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(config){
		config = config || {};
		Ext.apply(this,config);
		var _store = new Ext.data.JsonStore({
			root:"records",//返回的records开头的数据
			totalProperty:"totalRecords",//返回的是totalRecords的条数
			remoteSort:true,
			fields:com.zz91.zzwork.hr.DtoFields,
			url:Context.ROOT+"/feedback/queryFeedback.htm",
			autoLoad:true
		});
		var grid = this;
		var _sm=new Ext.grid.CheckboxSelectionModel();
		var _cm=new Ext.grid.ColumnModel([_sm,{
			header:"编号",
			width:30,
			sortable:true,
			dataIndex:"id",
			hidden:true
		},{
			header:"工号",
			sortable:false,
			dataIndex:"code"
		},{
			header:"姓名",
			sortable:false,
			dataIndex:"name"
		},{
			header:"出勤时间",
			sortable:false,
			dataIndex:"gmt_work"
		}]);
		var c = {
				store:_store,
				sm:_sm,
				cm:_cm,
				tbar:[{
					iconCls:"add16",
					text:"统计",
					handler:function(btn){
					
					}
				},'->','姓名:',{
					xtype:"textfield",
					iconCls:"delete16",
					name:"name",
					id:"name",
					handler:function(btn){
					
					}
				},'->','工号:',{
					xtype:"textfield",
					iconCls:"delete16",
					name:"code",
					id:"code",
					handler:function(btn){
					}
				},'->','考勤时间:',{
					xtype:"datefield",
					name:"gmtWork",
					id:"gmtWork",
					handler:function(btn){
					
					}
				}]
		};
		com.zz91.zzwork.hr.AttendanceTable.superclass.constructor.call(this, c);
	}
});

