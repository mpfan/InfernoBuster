import iptc
import os

table = iptc.Table(iptc.Table.FILTER)
in_chain = iptc.Chain(table, "INPUT")
out_chain = iptc.Chain(table, "OUTPUT")

#add rules
#dir - in/out
#decision - ACCEPT/DROP
def add_rule(proto, dir, decision, src_ip, dst_ip, src_port, dst_port):
    if dir == "in":
        chain = in_chain
    else:
        chain = out_chain

    if src_port and dst_port:
        rule = {'protocol': proto, 'src': src_ip, 'dst': dst_ip, 'target': decision, proto: {'sport': src_port, 'dport': dst_port}}
    elif src_port:
        rule = {'protocol': proto, 'src': src_ip, 'dst': dst_ip, 'target': decision, proto: {'sport': src_port}}
    elif dst_port:
        rule = {'protocol': proto, 'src': src_ip, 'dst': dst_ip, 'target': decision, proto: {'dport': dst_port}}
    else:
        rule = {'protocol': proto, 'src': src_ip, 'dst': dst_ip, 'target': decision}

    iptc.easy.insert_rule('filter', chain.name, rule)

#clean table
def start():
    os.system('sudo iptables --flush')
    #rule_d = {'src': '10.0.0.0/23', 'dst': '120.0.0.0/23', 'protocol': 'tcp', 'target': 'DROP', 'tcp': {'dport': '80'}}
    #iptc.easy.insert_rule('filter', out_chain.name, rule_d)

    add_rule('tcp', 'in', 'DROP', '11.0.0.0/23', '121.0.0.0/23', None, '80')
    


#os.system('iptables-save > ~/Desktop/internal_rules.v4')
