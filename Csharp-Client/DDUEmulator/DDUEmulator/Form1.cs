using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO.Ports;
using DDUEmulator.Properties;

namespace DDUEmulator
{
    public partial class Form1 : Form
    {
        SerialPort serialPortDDU;
        private Settings settings = Settings.Default;

        // Vars for logging
        System.IO.TextWriter tw;
        bool logFile = false;
        public enum LogMsgType { Incoming, Outgoing, Normal, Warning, Error };


        private double degreeDDU = 0;

        public Form1()
        {
            InitializeComponent();

            timerSentData.Enabled = true;
        }

        private void Log(LogMsgType msgtype, string msg)
        {
            rtfTerminal.Invoke(new EventHandler(delegate
            {
                rtfTerminal.SelectedText = string.Empty;
                rtfTerminal.SelectionFont = new Font(rtfTerminal.SelectionFont, FontStyle.Bold);
                //rtfTerminal.SelectionColor = LogMsgTypeColor[(int)msgtype];
                rtfTerminal.AppendText(msg);
                rtfTerminal.Text += (msg);
                //rtfTerminal.AppendText("\r\n");
                /*if (rtfTerminal.Text.Length > 0x100)
                {
                    rtfTerminal.Text = "";
                }*/
                //rtfTerminal.Text = (msg);
                rtfTerminal.ScrollToCaret();
            }));
        }

        private void TimerSentData_Tick(object sender, EventArgs e)
        {
            if (serialPortDDU.IsOpen)
            {
                Console.WriteLine("Running");
                
                String dataOut = "$tab02,3.5," + degreeDDU.ToString() +  ",90.1\r\n";

                if(degreeDDU <= 359.9)
                {
                    degreeDDU += 0.5;
                }

                serialPortDDU.Write(dataOut);
                Log(LogMsgType.Incoming, dataOut);
            }

        }

        private void Form1_Load(object sender, EventArgs e)
        {
            InitializeSerialPort();

            serialPortDDU.DataReceived += new SerialDataReceivedEventHandler(serialPort_DataReceived);
        }

        void serialPort_DataReceived(object sender, SerialDataReceivedEventArgs e)
        {
            try
            {
            }
            catch
            {

            }
        }

        private void InitializeSerialPort()
        {
            serialPortDDU = new SerialPort();
            int count = 0;
            foreach (string s in SerialPort.GetPortNames())
            {
                toolStripComboBoxPort.Items.Add(s);

                if (s == settings.Port)
                {
                    toolStripComboBoxPort.SelectedIndex = count;
                }
                count++;
            }

            if (toolStripComboBoxPort.Items.Count == 0)
            {
                toolStripComboBoxPort.Items.Add("No Device detected");
                toolStripBtnConnect.Enabled = false;

            }
        }

        private void ToolStripBtnConnect_Click(object sender, EventArgs e)
        {
            try
            {
                serialPortDDU.PortName = toolStripComboBoxPort.SelectedItem.ToString();
                serialPortDDU.BaudRate = 9600;
                serialPortDDU.Parity = Parity.None;
                serialPortDDU.DataBits = 8;
                serialPortDDU.StopBits = StopBits.One;
                serialPortDDU.Handshake = Handshake.None;
                serialPortDDU.NewLine = "\r";
                // now open the port
                serialPortDDU.Open();

                // display message
                toolStripBtnConnect.Enabled = false;
                toolStripBtnDisconnect.Enabled = true;


            }
            catch
            {


            }
        }

        private void ToolStripBtnDisconnect_Click(object sender, EventArgs e)
        {
            try
            {
                if (serialPortDDU.IsOpen)
                {
                    

                    serialPortDDU.Close();
                    toolStripBtnDisconnect.Enabled = false;
                    toolStripBtnConnect.Enabled = true;

                }
            }
            catch
            {
            }
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            try
            {
                settings.Port = toolStripComboBoxPort.SelectedItem.ToString();
                settings.Save();

            }
            catch
            {
            }
        }
    }
}
