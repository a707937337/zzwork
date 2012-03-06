Ext.namespace("com.zz91.zzwork.scheduler.report");

/**
 * 映射关系
 * */
com.zz91.zzwork.scheduler.report.Field=["id"];
/**
 * 汇总的日报周报列表，默认加载部门所有周报信息
 * */
com.zz91.zzwork.scheduler.report.Grid = Ext.extend(Ext.grid.GridPanel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		
		var _store = new Ext.data.JsonStore({
			root:"records",
			totalProperty:'totalRecords',
			remoteSort:true,
			fields:com.zz91.zzwork.scheduler.report.Field,
			url:Context.ROOT +   "/scheduler/report/queryReport.htm",
			autoLoad:false
		});
		
		var grid = this;
		var _sm=new Ext.grid.CheckboxSelectionModel({});
		
		var _cm=new Ext.grid.ColumnModel( {
			header : "年",
			width : 150,
			sortable : false,
			dataIndex : "year"
		},{
			header : "周",
			width : 150,
			sortable : false,
			dataIndex : "week"
		},[_sm,{
			header : "编号",
			width : 100,
			sortable : false,
			dataIndex : "code"
		},{
			header : "报告标题",
			width: 350,
			sortable : false,
			dataIndex : "title" 
		},{
			header :"汇报人",
			sortable : false,
			width : 150,
			dataIndex:"report",
			renderer:function(value, metaData, record, rowIndex, colIndex, store){
				var url=record.get("url");
				if(url!=null && typeof(url)!="undefined" && url.length>0){
					return "<a href='"+url+"' target='_blank' >"+value+"</a>" ;
				}
				return value;
			}
		}
		]);
		var c={
			loadMask:MESSAGE.loadmask,
			iconCls:"icon-grid",
			store:_store,
			sm:_sm,
			cm:_cm,
			tbar:this.mytoolbar,
			bbar: new Ext.PagingToolbar({
				pageSize : Context.PAGE_SIZE,
				store : _store,
				displayInfo: true,
				displayMsg: MESSAGE.paging.displayMsg,
				emptyMsg : MESSAGE.paging.emptyMsg,
				beforePageText : MESSAGE.paging.beforePageText,
				afterPageText : MESSAGE.paging.afterPageText,
				paramNames : MESSAGE.paging.paramNames
			})
		};
		
		com.zz91.zzwork.scheduler.report.Grid.superclass.constructor.call(this, c);
	},
	//加载员工所在部门的周报信息，默认加载本周周报信息
	loadreport:function(dept){
		this.getStore().reload({params:{"dept":dept}});
	},
	//初始化工具栏的年份选择和周选择
	initdate:function(year,week){
		
	},
	//改变选择可以查看到不同年.周的周报汇总
	mytoolbar:[{
		xtype:"combo" //年
	},{
		xtype:"combo" //周
	},{
		xtype:"checkbox" //选择是否只查看自己的周报
	}]
});


/**
 * 周报撰写表单，包含标题，年份，周等
 * */
com.zz91.zzwork.scheduler.report.Form=Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		var c={
			layout:"column",
			labelWidth:50,
			frame:true,
			labelAlign:"right",
			items:[{
				columnWidth:1,
				layout:"form",
				items:[{
					xtype:"textfield",
					anchor:"90%",
					fieldLabel:"标题",
					name:"title"
				}]
			},{
				columnWidth:0.5,
				layout:"form",
				items:[{
					xtype:"combo",
					fieldLabel:"年",
					triggerAction:"all",
					displayField:"v",
					anchor:"80%",
					store:{
						xtype:"jsonstore",
						url:Context.ROOT+"/data/year.js",
						//root:"years",
						fields:["k","v"]
					}
				}],
			},{
				columnWidth:0.5,
				layout:"form",
				border:false,
			    items:[{
					xtype:"combo",
					fieldLabel:"周",
					triggerAction:"all",
					anchor:"80%",
					displayField:"v",
					hiddenField:"k",
					store:{
						xtype:"jsonstore",
						url:Context.ROOT+"/data/year.js",
						autoLoad:true,
						fields:["k","v"]
					}
				}],	
			},{
				 columnWidth:1,
				 layout:"form",
				 border:false,
				 items:[{
					 xtype:"textarea",
					 hideLabel:true,
					 height:"500",
					 anchor:"99%"
				 }]
			}],
			buttons:[{text:"保存",handler:function(){alert("保存成功");}}]
		};
		com.zz91.zzwork.scheduler.report.Form.superclass.constructor.call(this, c);
	},
	//初始化工具栏的年份选择和周选择
	initdate:function(year,week){
		
	}
	
});

/**
 * 日/周报汇总时对应的日程信息
 * */
com.zz91.zzwork.scheduler.report.SchedulerGrid=Ext.extend(Ext.grid.EditorGridPanel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		var c={
			border:false,
			autoHeight: true,
			autoExpandColumn:2,
			sm:new Ext.grid.CheckboxSelectionModel(),
			store:new Ext.data.JsonStore({
					url:Context.ROOT+"/data/grid.js",
					root:"grid2",
					fields:["id","schedule","performance","importance"],
					autoLoad:true	
				}),
			cm:new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(),
					new Ext.grid.CheckboxSelectionModel(),
					{header:"日程",    dataIndex:"schedule",   width:600,sortable:true, editor:new Ext.form.TextField()},
					{header:"完成情况",dataIndex:"performance", width:150,sortable:true, editor:new Ext.form.TextField()},
					{header:"重要程度",dataIndex:"importance",  width:150,sortable:true, editor:new Ext.form.TextField()},
			]),
		};
		
		com.zz91.zzwork.scheduler.report.SchedulerGrid.superclass.constructor.call(this, c);
	},
	//加载我的日程信息，默认from,to是本周的起始时间，改变周报撰写的年/周可以改变from，to
	loadMyScheduler:function(account, from, to){
		
	},
	//加载周报相关的日程信息
	loadScheduler:function(reportId){
		
	},
	//撰写日/周报时允许手动删除某些日程，即允许不指定关联日程
	toolbar:[{
		iconCls:"delete16",
		text:"删除"
	}]
	
});


/**
 * 周报内容显示表单，当点击周报时用于显示周报详细内容
 * */
com.zz91.zzwork.scheduler.report.ContentForm=Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
		config = config||{};
		Ext.apply(this,config);
		var c={
			frame:true,
			items:[
				 {
				    layout:"column",
					border:false,
					labelWidth:20,
					labelAlign:"right",
					items:[
						{
							columnWidth:.5,
							layout:"form",
							border:false,
							items:[{
								xtype:"combo", 
								anchor:"90%",
								fieldLabel:"年",
								triggerAction:"all",
								displayField:"v",
								store:{
									xtype:"jsonstore",
									url:Context.ROOT+"/data/year.js",
									fields:["k","v"]
									}			
							}]
							
						},	
						{
							columnWidth:.5,
							layout:"form",
							border:false,
							items:[{
								xtype:"combo",
								anchor:"90%",
								triggerAction:"all",
								fieldLabel:"周",
								displayField:"v",
								store:{
									xtype:"jsonstore",
									url:Context.ROOT+"/data/year.js",
									fields:["k","v"]
									}
							}]
							
						}
					]		
				},
				{
					xtype:"editorgrid",
					autoHeight:true,
					autoExpandColumn:2,
					sm:new Ext.grid.CheckboxSelectionModel(),
					cm:new Ext.grid.ColumnModel([
							new Ext.grid.RowNumberer(),
							new Ext.grid.CheckboxSelectionModel(),
							{header:"报告标题",dataIndex:"title",width:250,sortable:true,editor:new Ext.form.TextField()},
							{header:"汇报人",dataIndex:"name",width:100,sortable:true,editor:new Ext.form.TextField()}//editor属性，是该区域可编辑,是EditorGridPanel与GridPanel唯一不同的地方
						]),
					store:new Ext.data.JsonStore({
							url:Context.ROOT+"/data/grid.js",
							root:"grid1",
							autoLoad:true,
							fields:["id","title","name"]
							}),
				},
				{
					 layout:"form",
					 style:"margin-top:20px",
					 border:false,
					 items:[{
					 	 xtype:"textarea",
					     emptyText:"输入总结内容",
					     hideLabel:true,
					     anchor:"100%",
						 height:400
					 }]
					
				}
			]
		};
		
		com.zz91.zzwork.scheduler.report.ContentForm.superclass.constructor.call(this, c);
	},
	//载入周报内容
	loadcontent:function(content){
		
	}
	
});

