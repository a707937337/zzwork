Ext.namespace('com.zz91.zzwork.hr');
com.zz91.zzwork.hr.CountFields=[
	{name:"id",mapping:"id"},
	{name:"name",mapping:"name"},
	{name:"code",mapping:"code"},
	{name:"account",mapping:"account"},
	{name:"punch0",mapping:"punch0"},
	{name:"punch20",mapping:"punch20"},
	{name:"punch40",mapping:"punch40"},
	{name:"punch60",mapping:"punch60"},
	{name:"punch80",mapping:"punch80"},
	{name:"punch_count",mapping:"punchCount"},
	{name:"gmt_month",mapping:"gmtMonth"},
	{name:"gmt_created",mapping:"gmtCreated"},
	{name:"gmt_modeified",mapping:"gmtModeified"}
];
com.zz91.zzwork.hr.AttCountTable = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(config){
	config = config || {};
	Ext.apply(this,config);
	var _store = new Ext.data.JsonStore({
		root:"records",//返回的records开头的数据
		totalProperty:"totalRecords",//返回的是totalRecords的条数
		remoteSort:true,
		fields:com.zz91.zzwork.hr.CountFields,
		url:Context.ROOT+"/feedback/queryFeedback.htm",
		autoLoad:true
	});
	var grid = this;
	var _sm=new Ext.grid.CheckboxSelectionModel();
	var _cm=new Ext.grid.ColumnModel([_sm,{
		header:"姓名",
		width:30,
		sortable:true,
		dataIndex:"name",
		hidden:true
	},{
		header:"无打卡记录",
		sortable:false,
		dataIndex:"punch0"
	},{
		header:"迟到/早退20分钟内",
		sortable:false,
		dataIndex:"punch20"
	},{
		header:"迟到/早退60分钟内",
		sortable:false,
		dataIndex:"punch60"
	},{
		header:"加班",
		sortable:false,
		dataIndex:"punch80"
	},{
		header:"迟到/早退总次数",
		sortable:false,
		dataIndex:"punch_count"
	}]);
	var c = {
			store:_store,
			sm:_sm,
			cm:_cm,
			tbar:[{
				iconCls:"add16",
				text:"考勤明细",
				handler:function(btn){
				
				}
			},'->','姓名:',{
				xtype:"textfield",
				iconCls:"delete16",
				name:"name",
				id:"name",
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
	com.zz91.zzwork.hr.AttCountTable.superclass.constructor.call(this, c);
}
});
