namespace DDUEmulator
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.timerSentData = new System.Windows.Forms.Timer(this.components);
            this.toolStrip2 = new System.Windows.Forms.ToolStrip();
            this.toolStripLabel2 = new System.Windows.Forms.ToolStripLabel();
            this.toolStripComboBoxPort = new System.Windows.Forms.ToolStripComboBox();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.toolStripBtnConnect = new System.Windows.Forms.ToolStripButton();
            this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
            this.toolStripBtnDisconnect = new System.Windows.Forms.ToolStripButton();
            this.toolStripSeparator3 = new System.Windows.Forms.ToolStripSeparator();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.rtfTerminal = new System.Windows.Forms.RichTextBox();
            this.toolStrip2.SuspendLayout();
            this.groupBox3.SuspendLayout();
            this.SuspendLayout();
            // 
            // timerSentData
            // 
            this.timerSentData.Interval = 200;
            this.timerSentData.Tick += new System.EventHandler(this.TimerSentData_Tick);
            // 
            // toolStrip2
            // 
            this.toolStrip2.ImageScalingSize = new System.Drawing.Size(32, 32);
            this.toolStrip2.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.toolStripLabel2,
            this.toolStripComboBoxPort,
            this.toolStripSeparator1,
            this.toolStripBtnConnect,
            this.toolStripSeparator2,
            this.toolStripBtnDisconnect,
            this.toolStripSeparator3});
            this.toolStrip2.Location = new System.Drawing.Point(0, 0);
            this.toolStrip2.Name = "toolStrip2";
            this.toolStrip2.Padding = new System.Windows.Forms.Padding(0, 0, 4, 0);
            this.toolStrip2.Size = new System.Drawing.Size(1033, 42);
            this.toolStrip2.TabIndex = 4;
            this.toolStrip2.Text = "toolStrip2";
            // 
            // toolStripLabel2
            // 
            this.toolStripLabel2.Name = "toolStripLabel2";
            this.toolStripLabel2.Size = new System.Drawing.Size(57, 36);
            this.toolStripLabel2.Text = "Port";
            // 
            // toolStripComboBoxPort
            // 
            this.toolStripComboBoxPort.Font = new System.Drawing.Font("Segoe UI", 9F);
            this.toolStripComboBoxPort.Name = "toolStripComboBoxPort";
            this.toolStripComboBoxPort.Size = new System.Drawing.Size(238, 42);
            // 
            // toolStripSeparator1
            // 
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            this.toolStripSeparator1.Size = new System.Drawing.Size(6, 42);
            // 
            // toolStripBtnConnect
            // 
            this.toolStripBtnConnect.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.toolStripBtnConnect.Image = global::DDUEmulator.Properties.Resources._001_06;
            this.toolStripBtnConnect.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.toolStripBtnConnect.Name = "toolStripBtnConnect";
            this.toolStripBtnConnect.Size = new System.Drawing.Size(46, 36);
            this.toolStripBtnConnect.Text = "toolStripButton1";
            this.toolStripBtnConnect.Click += new System.EventHandler(this.ToolStripBtnConnect_Click);
            // 
            // toolStripSeparator2
            // 
            this.toolStripSeparator2.Name = "toolStripSeparator2";
            this.toolStripSeparator2.Size = new System.Drawing.Size(6, 42);
            // 
            // toolStripBtnDisconnect
            // 
            this.toolStripBtnDisconnect.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.toolStripBtnDisconnect.Enabled = false;
            this.toolStripBtnDisconnect.Image = global::DDUEmulator.Properties.Resources._001_29;
            this.toolStripBtnDisconnect.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.toolStripBtnDisconnect.Name = "toolStripBtnDisconnect";
            this.toolStripBtnDisconnect.Size = new System.Drawing.Size(46, 36);
            this.toolStripBtnDisconnect.Text = "toolStripButton2";
            this.toolStripBtnDisconnect.Click += new System.EventHandler(this.ToolStripBtnDisconnect_Click);
            // 
            // toolStripSeparator3
            // 
            this.toolStripSeparator3.Name = "toolStripSeparator3";
            this.toolStripSeparator3.Size = new System.Drawing.Size(6, 42);
            // 
            // groupBox3
            // 
            this.groupBox3.Controls.Add(this.rtfTerminal);
            this.groupBox3.Location = new System.Drawing.Point(0, 58);
            this.groupBox3.Margin = new System.Windows.Forms.Padding(6);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.Padding = new System.Windows.Forms.Padding(6);
            this.groupBox3.Size = new System.Drawing.Size(1012, 175);
            this.groupBox3.TabIndex = 28;
            this.groupBox3.TabStop = false;
            this.groupBox3.Text = "Data out";
            // 
            // rtfTerminal
            // 
            this.rtfTerminal.Location = new System.Drawing.Point(12, 37);
            this.rtfTerminal.Margin = new System.Windows.Forms.Padding(6);
            this.rtfTerminal.Name = "rtfTerminal";
            this.rtfTerminal.Size = new System.Drawing.Size(984, 121);
            this.rtfTerminal.TabIndex = 9;
            this.rtfTerminal.Text = "";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 25F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1033, 714);
            this.Controls.Add(this.groupBox3);
            this.Controls.Add(this.toolStrip2);
            this.Name = "Form1";
            this.Text = "Form1";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Form1_FormClosing);
            this.Load += new System.EventHandler(this.Form1_Load);
            this.toolStrip2.ResumeLayout(false);
            this.toolStrip2.PerformLayout();
            this.groupBox3.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Timer timerSentData;
        private System.Windows.Forms.ToolStrip toolStrip2;
        private System.Windows.Forms.ToolStripLabel toolStripLabel2;
        private System.Windows.Forms.ToolStripComboBox toolStripComboBoxPort;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
        private System.Windows.Forms.ToolStripButton toolStripBtnConnect;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
        private System.Windows.Forms.ToolStripButton toolStripBtnDisconnect;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator3;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.RichTextBox rtfTerminal;
    }
}

