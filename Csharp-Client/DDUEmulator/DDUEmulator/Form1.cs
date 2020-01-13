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
        //System.IO.TextWriter tw;
        //bool logFile = false;
        public enum LogMsgType { Incoming, Outgoing, Normal, Warning, Error };

        //Instanciates the variable's
        private double degreeDDU = 0.00;
        private double inclinationDDU = 0.00;
        private double hsnDDU = 0.00;

        public Form1()
        {
            //Starts the Form
            InitializeComponent();

            //Sets the label text to a default value the gods will be happy this is one.
            label4.Text = "000.00";
            label5.Text = "000.00";
            label6.Text = "000.00";
            timerSentData.Enabled = true;

            // Fixes the border so it cannot be resized.
            FormBorderStyle = FormBorderStyle.FixedDialog;

            // Set the MaximizeBox to false to remove the option to maximize the window.
            MaximizeBox = false;

            // Set the MinimizeBox to false to remove the option to minimize the window
            MinimizeBox = false;

            // Set the start position of the form to the center of the screen.
            StartPosition = FormStartPosition.CenterScreen;
        }

        private void Log(LogMsgType msgtype, string msg)
        {
            rtfTerminal.Invoke(new EventHandler(delegate
            {
                rtfTerminal.SelectedText = string.Empty;
                rtfTerminal.SelectionFont = new Font(rtfTerminal.SelectionFont, FontStyle.Bold);
                rtfTerminal.AppendText(msg);
                rtfTerminal.Text += (msg);
                rtfTerminal.ScrollToCaret();
            }));
        }

        private void TimerSentData_Tick(object sender, EventArgs e)
        {
            if (serialPortDDU.IsOpen)
            {
                Console.WriteLine("Running");
                //counter++;
                //Sets the string that will be outputed via the serial controller with the inputed values
                String dataOut = "$tab02," + inclinationDDU.ToString().PadLeft(3, '0').PadRight(4, '.').PadRight(6, '0') + "," + degreeDDU.ToString().PadLeft(3, '0').PadRight(4, '.').PadRight(6, '0') + "," + hsnDDU.ToString().PadLeft(3, '0').PadRight(4, '.').PadRight(6, '0') + "\r\n";

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

        //sets the degree value to the relative position of the trackbar and displays it to the text of the label.
        private void trackBar1_Scroll(object sender, EventArgs e)
        {
            degreeDDU = trackBar1.Value;
            label4.Text = degreeDDU.ToString().PadLeft(3, '0').PadRight(4, '.').PadRight(6, '0');
        }

        //sets the Hightside value to the relative position of the trackbar and displays it to the text of the label.
        private void trackBar2_Scroll(object sender, EventArgs e)
        {
            hsnDDU = trackBar2.Value;
            label5.Text = hsnDDU.ToString().PadLeft(3, '0').PadRight(4, '.').PadRight(6, '0');
        }

        //sets the inclination value to the relative position of the trackbar and displays it to the text of the label.
        private void trackBar3_Scroll(object sender, EventArgs e)
        {
            inclinationDDU = trackBar3.Value;
            label6.Text = inclinationDDU.ToString().PadLeft(3, '0').PadRight(4, '.').PadRight(6, '0');
        }
    }
}
