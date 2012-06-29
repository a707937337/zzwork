Ext.namespace('com.zz91.zzwork.hr');

com.zz91.zzwork.hr.ImptForm = Ext.extend(Ext.form.FormPanel,{
	constructor:function(config){
		config = config || {};
		Ext.apply(this,config);
		
		var form=this;
		
		var c = {
			layout:'column',
			frame:true,
			fileUpload:true, 
			labelAlign:'right',
			labelWidth:100,
			items : [{
				layout:"form",
				columnWidth:.5,
				defaults:{
					anchor:"100%",
					xtype:"datefield"
				},
				items:[{
					allowBlank:false,
					fieldLabel:"开始日期",
					format:"y-m-d 00:00:00",
					name:"from",
					id:"from"
				}]
			},{
				layout:"form",
				columnWidth:.5,
				defaults:{
					anchor:"100%",
					xtype:"datefield"
				},
				items:[{
					allowBlank:false,
					fieldLabel:"截至日期",
					format:"y-m-d 00:00:00",
					name:"to",
					id:"to"
				}]
			},{
				layout:"form",
				columnWidth:1,
				defaults:{
					anchor:"100%"
				},
				items:[{
					xtype:'textfield',
					inputType:'file',
					name:'uploadExcel',
					anchor:"100%",
					fieldLabel:"选择文件"
					
				}]
			}],
			buttons:[{
				layout:"form",
				iconCls:"add32",
				scale:"large",
				text:"导入数据",
				handler:function(){
				form.getForm().submit({
					url:Context.ROOT+"/hr/attendance/doImpt.htm",
					method:"post",
					//waitMsg : "正在上传,请稍后！",
					type:"json",
					success:Context.ROOT+"/hr/attendance/query.htm",
					failure:function(_form,_action){
						Ext.MessageBox.show({
							title:MESSAGE.title,
							msg : _action.result.data,
							buttons:Ext.MessageBox.OK,
							icon:Ext.MessageBox.ERROR
						});
					}
				});
				

				}
			}]
		};
		
		com.zz91.zzwork.hr.ImptForm.superclass.constructor.call(this,c);
	},
	initFocus:function(){
		this.findById("from").focus(true,100);
	}
});
