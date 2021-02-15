import os, subprocess, sys
import platform

def add_rule(name, dir, action, src_ip, dst_ip, src_port, dst_port):
    cmd = 'netsh advfirewall firewall add rule name={} dir={} action={} enable=yes localip="{}" remoteip="{}" localport="{}" remoteport="{}" protocol=tcp profile=any'
    local_port = src_port if src_port else "any"
    remoteport = dst_port if dst_port else "any"

    cmd_rule = cmd.format(name, dir, action, src_ip, dst_ip, local_port, remoteport)
    os.popen(cmd_rule)

#cmd = 'netsh advfirewall firewall add rule name="{}" dir={} action={} enable=yes localip="{}" remoteip="{}" localport="{}" remoteport="{}" protocol=tcp profile=any'

#export a copy of firewall rule
def export():
    subprocess.Popen(["powershell.exe", 'reg export HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services\\SharedAccess\\Parameters\\FirewallPolicy\\FirewallRules c:\\Users\\User\\Documents\\internal_rules.reg'], stdout=sys.stdout).communicate()

def export2():
    subprocess.Popen(["powershell.exe", 'reg export HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Services\\SharedAccess\\Parameters\\FirewallPolicy\\FirewallRules c:\\Users\\User\\Documents\\win_external_rules.reg'], stdout=sys.stdout).communicate()