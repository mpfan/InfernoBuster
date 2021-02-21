#import
import ufw_gen as ufw
import win_gen as win
import iptb_gen as iptb

#rule entity
class entity:
    def __init__(self, win_ip, lin_ip, src_port, dst_port):
        self.win_ip = win_ip
        self.lin_ip = lin_ip
        self.src_port = src_port
        self.dst_port = dst_port

    def lin_port(self, port):
        if not port:
            return port
        if len(port) == 0:
            return port[0]
        
        s = ""
        for x in port:
            if port.index(x) != len(port) - 1:
                s += x + ","
            else:
                s += x

        return s

#DMZ
#src_port - the port in which the message should be received
#dst port - the reply
web = entity("125.168.0.0-126.168.3.255", "125.158.0.0/22", ["80", "8080", "403"], None)
email = entity("125.168.4.0-125.168.7.255", "125.168.4.0/22", ["25", "110", "995"], None)
ftp = entity("125.168.7.0-125.168.7.255", "125.168.7.0/24", ["23"], None)
dns = entity("125.168.8.0-125.168.9.255", "125.168.8.0/23", ["53"], None)
proxy = entity("125.168.10.0-125.168.10.255", "125.168.10.0/24", ["53"], None)

#Internal
#no ports
sales = entity("125.168.11.0-125.168.11.255", "125.168.11.0/24", None, None)
hr = entity("125.168.12.0-125.168.12.255", "125.168.12.0/24", None, None)
accounting = entity("125.168.13.0-125.168.13.255", "125.168.13.0/24", None, None)
devs = entity("125.168.14.0-125.168.15.255", "125.168.14.0/23", None, None)
prod = entity("125.168.16.0-125.168.17.255", "125.168.16.0/23", None, None)
rd = entity("125.168.18.0-125.168.19.255", "125.168.18.0/23", None, None)
eng = entity("125.168.20.0-125.168.21.255", "125.168.20.0/23", None, None)
internal = entity("125.168.22.0-125.168.23.255", "125.168.22.0/23", None, None)
quality = entity("125.168.24.0-125.168.24.255", "125.168.24.0/24", None, None)
it = entity("125.168.25.0-125.168.25.255", "125.168.25.0/24", None, None)
test_net = entity("125.168.26.0-125.168.26.255", "125.168.26.0/24", None, None)

#External service
azure = entity("13.64.0.0-13.127.255.255", "13.64.0.0/10", ["80", "8080", "403"], ["2500"])
google = entity("172.217.0.0-172.217.255.255", "172.217.0.0/16", ["80", "8080", "403"], ["2500"])
stackoverflow = entity("151.101.0.0-151.101.255.255", "151.101.0.0/16", ["80", "8080", "403"], ["2500"])
ms = entity("40.64.0.0-40.127.255.255", "40.74.0.0/10", ["80", "8080", "403"], ["2500"])
wiki = entity("208.80.152.0-208.80.155.255", "208.80.152.0/22", ["80", "8080", "403"], ["2500"])
external = [azure, google, stackoverflow, ms, wiki]

#vpn clients
vpn1 = entity("172.2.1.3", "172.2.1.3", None, ["53", "443", "110", "80", "502", "501"])
vpn2 = entity("172.2.10.0-172.2.11.255", "172.2.10.0/23", None, ["53", "443", "110", "80", "502", "501"])
vpn3 = entity("192.0.3.1", "192.0.3.1", None, ["53", "443", "110", "80", "502", "501"])
vpn4 = entity("198.10.55.0-198.10.55.255", "198.10.55.0/23", None, ["53", "443", "110", "80", "502", "501"])
vpn = [vpn1, vpn2, vpn3, vpn4]

#Internal
# sales, hr, accounting
#   web, email, dns, external services
#
# dev, prod, rd, eng, internal, it
#   web, email, ftp, dns, proxy, external services
#
# quality, test
#   web, email, ftp, dns
#


#External
# + internal
# 
# + anomalies
#
# VPN (external only)
#   DNS


#Windows
def win_internal_reply_response(client_name, client, services):
    x = 0
    for service in services:
        y = 0
        for src_port in service.src_port:
            win.add_rule(client_name + "_service" + str(x) + "." + str(y), "out", "allow", client.win_ip, service.win_ip, None, src_port)
            y += 1
        win.add_rule("service_" + client_name + str(x), "in", "allow", service.win_ip, client.win_ip, None, None)
        x += 1

def win_external_reply_response(client_name, client, decision):
    x = 0
    for service in external:
        y = 0
        for src_port in service.src_port:
            win.add_rule(client_name + "_external_service" + str(x) + "." + str(y), "out", decision, client.win_ip, service.win_ip, None, src_port)
            y += 1

        y = 0
        for dst_port in service.dst_port:
            win.add_rule("external_service_" + client_name + str(x) + "." + str(y), "in", decision, service.win_ip, client.win_ip, None, dst_port)
            y += 1
            
        x += 1

def internal_fire_win():
    # sales, hr, accounting
    win_internal_reply_response("sales", sales, [web, email, dns]) #sales
    win_external_reply_response("sales", sales, "allow")
    win_internal_reply_response("hr", hr, [web, email, dns]) #hr
    win_external_reply_response("hr", hr, "allow")
    win_internal_reply_response("accounting", accounting, [web, email, dns]) #accounting
    win_external_reply_response("accounting", accounting, "allow")

    # dev, prod, rd, eng, internal, it
    win_internal_reply_response("dev", devs, [web, email, ftp, dns, proxy]) #dev
    win_external_reply_response("dev", devs, "allow")
    win_internal_reply_response("prod", prod, [web, email, ftp, dns, proxy]) #prod
    win_external_reply_response("prod", prod, "allow")
    win_internal_reply_response("rd", rd, [web, email, ftp, dns, proxy]) #rd
    win_external_reply_response("rd", rd, "allow")
    win_internal_reply_response("eng", eng, [web, email, ftp, dns, proxy]) #eng
    win_external_reply_response("eng", eng, "allow")
    win_internal_reply_response("internal", internal, [web, email, ftp, dns, proxy]) #internal
    win_external_reply_response("internal", internal, "allow")
    win_internal_reply_response("it", it, [web, email, ftp, dns, proxy]) #it
    win_external_reply_response("it", it, "allow")

    #quality, testing
    win_internal_reply_response("test", test_net, [web, email, ftp, dns]) #test
    win_internal_reply_response("quality", quality, [web, email, ftp, dns]) #quality

def external_fire_win():
    #VPN clients
    x = 0
    for v in vpn:
        y = 0
        for dst_port in v.dst_port:
            win.add_rule("vpn" + str(x) + "." + str(y), "in", "allow", v.win_ip, dns.win_ip, None, dst_port)
            y += 1
        x += 1

    #Redundant
    #same rules
    win_external_reply_response("redundant_accounting", accounting, "allow")

    #inconsistent
    #rules where they have the same packet but different action
    win_external_reply_response("inconsistent_prod", prod, "block")
    
    #correlation
    #rules that intersect with another but defines a different action
    devs_prod_corr = entity("125.168.15.0-125.168.16.255", None, None, None)
    win_external_reply_response("devs_prod_corr", devs_prod_corr, "block")

    #partial_redundancy
    #rules that intersect with another but defines the same action
    rd_eng_partial_redundancy = entity("125.168.19.0-125.168.20.255", None, None, None)
    win_external_reply_response("rd_eng_partial_redundancy", rd_eng_partial_redundancy, "allow")

    #generalization
    #rules where one is a subset of another but they have different actions and the superset rule has a higher priority
    google2 = entity("172.217.0.0-172.217.127.255", "172.217.0.0/17", ["80", "8080", "403"], ["2500"])
    x = 0
    for src_port in google2.src_port:
        win.add_rule("generalization_prod_google" + str(x), "out", "block", prod.win_ip, google2.win_ip, None, src_port)
        x += 1
    win.add_rule("generalization_google_prod", "in", "block", google2.win_ip, prod.win_ip, None, google.dst_port)

    #up_redundancy
    #rules where one is a subset of another but they have the same actions and the superset rule has a higher priority
    stackoverflow2 = entity("151.101.0.0-151.101.127.255", "151.101.0.0/17", ["80", "8080", "403"], ["2500"])
    x = 0
    for src_port in stackoverflow2.src_port:
        win.add_rule("up_redundancy_it_stackoverflow" + str(x), "out", "allow", it.win_ip, stackoverflow2.win_ip, None, src_port)
        x += 1
    win.add_rule("up_redundancy_stackoverflow_it", "in", "allow", stackoverflow2.win_ip, it.win_ip, None, stackoverflow2.dst_port)
    
    #shadowing
    #rules where one is a subset of another but they have different actions and the subset rule has a higher priority
    wiki2 = entity("208.80.144.0-208.80.159.255", "208.80.152.0/20", ["80", "8080", "403"], ["2500"])
    x = 0
    for src_port in stackoverflow2.src_port:
        win.add_rule("shadowing_it_wiki" + str(x), "out", "block", it.win_ip, wiki2.win_ip, None, src_port)
        x += 1
    win.add_rule("shadowing_wiki_it", "in", "block", wiki2.win_ip, it.win_ip, None, wiki2.dst_port)

    #down_redundancy
    #rules where one is a subset of another but they have the same actions and the subset rule has a higher priority
    proxy2 = entity("125.168.10.0-125.168.11.255", "125.168.10.0/23", ["53"], None)
    win_external_reply_response("down_redundancy", proxy2, "allow")

#UFW
def ufw_reply_response(client, services, decision):
    for service in services:
        for port in service.src_port:
            ufw.add_rule(decision, client.lin_ip, service.lin_ip, port)
        ufw.add_rule(decision, service.lin_ip, client.lin_ip, service.lin_port(service.dst_port))

def internal_fire_ufw():
    # sales, hr, accounting
    ufw_reply_response(sales, [web, email, dns] + external, "allow")
    ufw_reply_response(hr, [web, email, dns] + external, "allow")
    ufw_reply_response(accounting, [web, email, dns] + external, "allow")

    #dev, prod, rd, eng, internal, it
    ufw_reply_response(devs, [web, email, ftp, dns, proxy] + external, "allow")
    ufw_reply_response(prod, [web, email, ftp, dns, proxy] + external, "allow")
    ufw_reply_response(rd, [web, email, ftp, dns, proxy] + external, "allow")
    ufw_reply_response(eng, [web, email, ftp, dns, proxy] + external, "allow")
    ufw_reply_response(internal, [web, email, ftp, dns, proxy] + external, "allow")
    ufw_reply_response(it, [web, email, ftp, dns, proxy] + external, "allow")
    
    #quality , testing
    ufw_reply_response(test_net, [web, email, ftp, dns], "allow")
    ufw_reply_response(quality, [web, email, ftp, dns], "allow")

def external_fire_ufw():
    #VPN clients
    for v in vpn:
        for port in ["53","443","110","80","502","501","80"]:
            ufw.add_rule("allow", v.lin_ip, dns.lin_ip, port)

    #Redundant
    #same rules
    ufw.add_rule("allow", sales.lin_ip, web.lin_ip, None)
    ufw.add_rule("allow", sales.lin_ip, email.lin_ip, None)

    #inconsistent
    #rules where they have the same packet but different action
    ufw.add_rule("deny", sales.lin_ip, dns.lin_ip, None)
    ufw.add_rule("deny", sales.lin_ip, azure.lin_ip, None)
    
    #correlation
    #rules that intersect with another but defines a different action
    ufw.add_rule("allow", "125.168.27.13", "10.0.0.2", "223")
    ufw.add_rule2("deny", "125.168.27.13", "10.0.0.2", "223")
    ufw.add_rule("allow", "125.168.27.13", "10.0.0.33", "223")
    ufw.add_rule2("deny", "125.168.27.13", "10.0.0.33", "223")


    #partial_redundancy
    #rules that intersect with another but defines the same action
    ufw.add_rule("deny", "125.168.14.5", web.lin_ip, "80") #subset of dev
    ufw.add_rule("deny", "125.168.14.12", web.lin_ip, "80") #subset of dev
    ufw.add_rule2("deny", "125.168.14.12", web.lin_ip, "80") #subset of dev
    ufw.add_rule2("deny", "125.168.14.24", web.lin_ip, "80") #subset of dev


    #generalization
    #rules where one is a subset of another but they have different actions and the superset rule has a higher priority
    ufw.add_rule("deny", "125.168.14.0/24", web.lin_ip, None) #subset of dev
    ufw.add_rule("deny", devs.lin_ip, "125.158.0.0/23", None) #subset of web
    ufw.add_rule("deny", "125.168.14.0/24", "125.158.0.0/24", "143") #subset of dev

    #up_redundancy
    #rules where one is a subset of another but they have the same actions and the superset rule has a higher priority
    ufw.add_rule("allow", "125.168.20.0/24", email.lin_ip, None)

    #shadowing
    #rules where one is a subset of another but they have different actions and the subset rule has a higher priority
    ufw.add_rule("deny", "125.168.24.0/23", email.lin_ip, None) #subset of quality and it

    #down_redundancy
    #rules where one is a subset of another but they have the same actions and the subset rule has a higher priority
    ufw.add_rule("deny", prod.lin_ip, "125.158.0.0/21", None) #subset of web and email

#IPTABLES
def iptb_reply_response(proto, client, services, decision):
    for service in services:
        for port in service.src_port:
            iptb.add_rule(proto, "out", decision, client.lin_ip, service.lin_ip, None, port)
        iptb.add_rule(proto, "in", decision, service.lin_ip, client.lin_ip, None, service.dst_port)

def internal_fire_iptb():
    # sales, hr, accounting
    iptb_reply_response('udp', sales, [web, email, dns], "ACCEPT")
    iptb_reply_response('tcp', sales, external, "ACCEPT")
    iptb_reply_response('udp', hr, [web, email, dns], "ACCEPT")
    iptb_reply_response('tcp', hr, external, "ACCEPT")
    iptb_reply_response('udp', accounting, [web, email, dns], "ACCEPT")
    iptb_reply_response('tcp', accounting, external, "ACCEPT")

    #dev, prod, rd, eng, internal, it
    iptb_reply_response('udp', devs, [web, email, ftp, dns, proxy], "ACCEPT")
    iptb_reply_response('tcp', devs, external, "ACCEPT")
    iptb_reply_response('udp', prod, [web, email, ftp, dns, proxy], "ACCEPT")
    iptb_reply_response('tcp', prod, external, "ACCEPT")
    iptb_reply_response('udp', rd, [web, email, ftp, dns, proxy], "ACCEPT")
    iptb_reply_response('tcp', rd, external, "ACCEPT")
    iptb_reply_response('udp', eng, [web, email, ftp, dns, proxy], "ACCEPT")
    iptb_reply_response('tcp', eng, external, "ACCEPT")
    iptb_reply_response('udp', internal, [web, email, ftp, dns, proxy], "ACCEPT")
    iptb_reply_response('tcp', internal, external, "ACCEPT")
    iptb_reply_response('udp', it, [web, email, ftp, dns, proxy], "ACCEPT")
    iptb_reply_response('tcp', it, external, "ACCEPT")

    #quality , testing
    iptb_reply_response('udp', quality, [web, email, ftp, dns], "ACCEPT")
    iptb_reply_response('udp', test_net, [web, email, ftp, dns], "ACCEPT")

def external_fire_iptb():
    #VPN clients
    for v in vpn:
        for port in ["53","443","110","80","502","501","80"]:
            iptb.add_rule("tcp", "in", "ACCEPT", v.lin_ip, dns.lin_ip, None, port)

    #inconsistent
    #rules where they have the same packet but different action
    iptb.add_rule("udp", "out", "DROP", sales.lin_ip, dns.lin_ip, None, None)
    iptb.add_rule("tcp", "out", "DROP", sales.lin_ip, azure.lin_ip, None, None)

    #correlation
    #rules that intersect with another but defines a different action
    iptb.add_rule("tcp", "out", "ACCEPT", "125.168.27.13", "10.0.0.2", None, None)
    iptb.add_rule("tcp", "out", "DROP", "125.168.27.13", "10.0.0.2", None, None)
    iptb.add_rule("tcp", "out", "ACCEPT", "125.168.27.13", "10.0.0.33", None, None)
    iptb.add_rule("tcp", "out", "DROP", "125.168.27.13", "10.0.0.33", None, None)

    #partial_redundancy
    #rules that intersect with another but defines the same action
    iptb.add_rule("udp", "out", "DROP", "125.168.14.5", web.lin_ip, None, "80")
    iptb.add_rule("udp", "out", "DROP", "125.168.27.12", web.lin_ip, None, "80")

    #generalization
    #rules where one is a subset of another but they have different actions and the superset rule has a higher priority
    iptb.add_rule("udp", "out", "DROP", "125.168.14.0/24", web.lin_ip, None, None)
    iptb.add_rule("udp", "out", "DROP", devs.lin_ip, "125.158.0.0/23", None, None)
    iptb.add_rule("udp", "out", "DROP", "125.168.14.0/24", "125.158.0.0/24", None, "143")
    
    #up_redundancy
    #rules where one is a subset of another but they have the same actions and the superset rule has a higher priority
    iptb.add_rule("udp", "out", "ACCEPT", "125.168.20.0/24", email.lin_ip, None, None)

def main():
    firewall = input("win = 0, ufw = 1, iptable = 2, iptables2 = 3\n")

    if firewall == "0":
        internal_fire_win()
        win.export()
        external_fire_win()
        win.export2()
    elif firewall == "1":
        ufw.start()
        internal_fire_ufw()
        ufw.export_internal()
        external_fire_ufw()
        ufw.export_external()
    elif firewall == "2":
        iptb.start()
        internal_fire_iptb()
    else:
        external_fire_iptb()

if __name__ == "__main__":
    main()