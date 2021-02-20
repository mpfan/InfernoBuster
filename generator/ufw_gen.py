import pyufw as ufw
import os

#add rule
#decision = allow or deny
#src_ip
#
def add_rule(decision, src_ip, dst_ip, dst_port):
    cmd = decision + " proto tcp from " + src_ip + " to " + dst_ip

    if dst_port:
        cmd += " port " + dst_port
    
    ufw.add(cmd)

def add_rule2(decision, src_ip, dst_ip, dst_port):
    cmd = decision + " proto udp from " + src_ip + " to " + dst_ip

    if dst_port:
        cmd += " port " + dst_port
    
    ufw.add(cmd) 

#start
def start():
    ufw.enable()
    ufw.reset()


#export internal
def export_internal():
    os.system("cp /etc/ufw/user.rules internal_rules")
    os.system("chmod +rwx internal_rules")


#export external
def export_external():
    os.system("cp /etc/ufw/user.rules external_rules")
    os.system("chmod +rwx external_rules")
