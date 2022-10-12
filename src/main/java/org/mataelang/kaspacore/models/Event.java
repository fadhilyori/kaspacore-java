package org.mataelang.kaspacore.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "seconds",
        "action",
        "class",
        "dir",
        "dst_addr",
        "dst_ap",
        "dst_port",
        "eth_dst",
        "eth_len",
        "eth_src",
        "eth_type",
        "gid",
        "iface",
        "ip_id",
        "ip_len",
        "msg",
        "mpls",
        "pkt_gen",
        "pkt_len",
        "pkt_num",
        "priority",
        "proto",
        "rev",
        "rule",
        "service",
        "sid",
        "src_addr",
        "src_ap",
        "src_port",
        "tcp_ack",
        "tcp_flags",
        "tcp_len",
        "tcp_seq",
        "tcp_win",
        "tos",
        "ttl",
        "vlan",
        "timestamp"
})
@Generated("jsonschema2pojo")
public class Event implements Serializable
{

    @JsonProperty("seconds")
    private Integer seconds;
    @JsonProperty("action")
    private String action;
    @JsonProperty("class")
    private String _class;
    @JsonProperty("dir")
    private String dir;
    @JsonProperty("dst_addr")
    private String dstAddr;
    @JsonProperty("dst_ap")
    private String dstAp;
    @JsonProperty("dst_port")
    private Integer dstPort;
    @JsonProperty("eth_dst")
    private String ethDst;
    @JsonProperty("eth_len")
    private Integer ethLen;
    @JsonProperty("eth_src")
    private String ethSrc;
    @JsonProperty("eth_type")
    private String ethType;
    @JsonProperty("gid")
    private Integer gid;
    @JsonProperty("iface")
    private String iface;
    @JsonProperty("ip_id")
    private Integer ipId;
    @JsonProperty("ip_len")
    private Integer ipLen;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("mpls")
    private Integer mpls;
    @JsonProperty("pkt_gen")
    private String pktGen;
    @JsonProperty("pkt_len")
    private Integer pktLen;
    @JsonProperty("pkt_num")
    private Integer pktNum;
    @JsonProperty("priority")
    private Integer priority;
    @JsonProperty("proto")
    private String proto;
    @JsonProperty("rev")
    private Integer rev;
    @JsonProperty("rule")
    private String rule;
    @JsonProperty("service")
    private String service;
    @JsonProperty("sid")
    private Integer sid;
    @JsonProperty("src_addr")
    private String srcAddr;
    @JsonProperty("src_ap")
    private String srcAp;
    @JsonProperty("src_port")
    private Integer srcPort;
    @JsonProperty("tcp_ack")
    private Long tcpAck;
    @JsonProperty("tcp_flags")
    private String tcpFlags;
    @JsonProperty("tcp_len")
    private Integer tcpLen;
    @JsonProperty("tcp_seq")
    private Integer tcpSeq;
    @JsonProperty("tcp_win")
    private Integer tcpWin;
    @JsonProperty("tos")
    private Integer tos;
    @JsonProperty("ttl")
    private Integer ttl;
    @JsonProperty("vlan")
    private Integer vlan;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 2279626189003175701L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Event() {
    }

    /**
     *
     * @param ethDst
     * @param msg
     * @param gid
     * @param pktLen
     * @param rule
     * @param srcPort
     * @param dir
     * @param pktGen
     * @param sid
     * @param ethLen
     * @param seconds
     * @param dstPort
     * @param ipId
     * @param vlan
     * @param ipLen
     * @param pktNum
     * @param action
     * @param mpls
     * @param tos
     * @param ethType
     * @param tcpFlags
     * @param timestamp
     * @param rev
     * @param tcpWin
     * @param dstAp
     * @param ethSrc
     * @param tcpSeq
     * @param priority
     * @param tcpLen
     * @param ttl
     * @param iface
     * @param tcpAck
     * @param srcAddr
     * @param service
     * @param proto
     * @param dstAddr
     * @param srcAp
     * @param _class
     */
    public Event(Integer seconds, String action, String _class, String dir, String dstAddr, String dstAp, Integer dstPort, String ethDst, Integer ethLen, String ethSrc, String ethType, Integer gid, String iface, Integer ipId, Integer ipLen, String msg, Integer mpls, String pktGen, Integer pktLen, Integer pktNum, Integer priority, String proto, Integer rev, String rule, String service, Integer sid, String srcAddr, String srcAp, Integer srcPort, Long tcpAck, String tcpFlags, Integer tcpLen, Integer tcpSeq, Integer tcpWin, Integer tos, Integer ttl, Integer vlan, String timestamp) {
        super();
        this.seconds = seconds;
        this.action = action;
        this._class = _class;
        this.dir = dir;
        this.dstAddr = dstAddr;
        this.dstAp = dstAp;
        this.dstPort = dstPort;
        this.ethDst = ethDst;
        this.ethLen = ethLen;
        this.ethSrc = ethSrc;
        this.ethType = ethType;
        this.gid = gid;
        this.iface = iface;
        this.ipId = ipId;
        this.ipLen = ipLen;
        this.msg = msg;
        this.mpls = mpls;
        this.pktGen = pktGen;
        this.pktLen = pktLen;
        this.pktNum = pktNum;
        this.priority = priority;
        this.proto = proto;
        this.rev = rev;
        this.rule = rule;
        this.service = service;
        this.sid = sid;
        this.srcAddr = srcAddr;
        this.srcAp = srcAp;
        this.srcPort = srcPort;
        this.tcpAck = tcpAck;
        this.tcpFlags = tcpFlags;
        this.tcpLen = tcpLen;
        this.tcpSeq = tcpSeq;
        this.tcpWin = tcpWin;
        this.tos = tos;
        this.ttl = ttl;
        this.vlan = vlan;
        this.timestamp = timestamp;
    }

    @JsonProperty("seconds")
    public Integer getSeconds() {
        return seconds;
    }

    @JsonProperty("seconds")
    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("class")
    public String getClass_() {
        return _class;
    }

    @JsonProperty("class")
    public void setClass_(String _class) {
        this._class = _class;
    }

    @JsonProperty("dir")
    public String getDir() {
        return dir;
    }

    @JsonProperty("dir")
    public void setDir(String dir) {
        this.dir = dir;
    }

    @JsonProperty("dst_addr")
    public String getDstAddr() {
        return dstAddr;
    }

    @JsonProperty("dst_addr")
    public void setDstAddr(String dstAddr) {
        this.dstAddr = dstAddr;
    }

    @JsonProperty("dst_ap")
    public String getDstAp() {
        return dstAp;
    }

    @JsonProperty("dst_ap")
    public void setDstAp(String dstAp) {
        this.dstAp = dstAp;
    }

    @JsonProperty("dst_port")
    public Integer getDstPort() {
        return dstPort;
    }

    @JsonProperty("dst_port")
    public void setDstPort(Integer dstPort) {
        this.dstPort = dstPort;
    }

    @JsonProperty("eth_dst")
    public String getEthDst() {
        return ethDst;
    }

    @JsonProperty("eth_dst")
    public void setEthDst(String ethDst) {
        this.ethDst = ethDst;
    }

    @JsonProperty("eth_len")
    public Integer getEthLen() {
        return ethLen;
    }

    @JsonProperty("eth_len")
    public void setEthLen(Integer ethLen) {
        this.ethLen = ethLen;
    }

    @JsonProperty("eth_src")
    public String getEthSrc() {
        return ethSrc;
    }

    @JsonProperty("eth_src")
    public void setEthSrc(String ethSrc) {
        this.ethSrc = ethSrc;
    }

    @JsonProperty("eth_type")
    public String getEthType() {
        return ethType;
    }

    @JsonProperty("eth_type")
    public void setEthType(String ethType) {
        this.ethType = ethType;
    }

    @JsonProperty("gid")
    public Integer getGid() {
        return gid;
    }

    @JsonProperty("gid")
    public void setGid(Integer gid) {
        this.gid = gid;
    }

    @JsonProperty("iface")
    public String getIface() {
        return iface;
    }

    @JsonProperty("iface")
    public void setIface(String iface) {
        this.iface = iface;
    }

    @JsonProperty("ip_id")
    public Integer getIpId() {
        return ipId;
    }

    @JsonProperty("ip_id")
    public void setIpId(Integer ipId) {
        this.ipId = ipId;
    }

    @JsonProperty("ip_len")
    public Integer getIpLen() {
        return ipLen;
    }

    @JsonProperty("ip_len")
    public void setIpLen(Integer ipLen) {
        this.ipLen = ipLen;
    }

    @JsonProperty("msg")
    public String getMsg() {
        return msg;
    }

    @JsonProperty("msg")
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @JsonProperty("mpls")
    public Integer getMpls() {
        return mpls;
    }

    @JsonProperty("mpls")
    public void setMpls(Integer mpls) {
        this.mpls = mpls;
    }

    @JsonProperty("pkt_gen")
    public String getPktGen() {
        return pktGen;
    }

    @JsonProperty("pkt_gen")
    public void setPktGen(String pktGen) {
        this.pktGen = pktGen;
    }

    @JsonProperty("pkt_len")
    public Integer getPktLen() {
        return pktLen;
    }

    @JsonProperty("pkt_len")
    public void setPktLen(Integer pktLen) {
        this.pktLen = pktLen;
    }

    @JsonProperty("pkt_num")
    public Integer getPktNum() {
        return pktNum;
    }

    @JsonProperty("pkt_num")
    public void setPktNum(Integer pktNum) {
        this.pktNum = pktNum;
    }

    @JsonProperty("priority")
    public Integer getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @JsonProperty("proto")
    public String getProto() {
        return proto;
    }

    @JsonProperty("proto")
    public void setProto(String proto) {
        this.proto = proto;
    }

    @JsonProperty("rev")
    public Integer getRev() {
        return rev;
    }

    @JsonProperty("rev")
    public void setRev(Integer rev) {
        this.rev = rev;
    }

    @JsonProperty("rule")
    public String getRule() {
        return rule;
    }

    @JsonProperty("rule")
    public void setRule(String rule) {
        this.rule = rule;
    }

    @JsonProperty("service")
    public String getService() {
        return service;
    }

    @JsonProperty("service")
    public void setService(String service) {
        this.service = service;
    }

    @JsonProperty("sid")
    public Integer getSid() {
        return sid;
    }

    @JsonProperty("sid")
    public void setSid(Integer sid) {
        this.sid = sid;
    }

    @JsonProperty("src_addr")
    public String getSrcAddr() {
        return srcAddr;
    }

    @JsonProperty("src_addr")
    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    @JsonProperty("src_ap")
    public String getSrcAp() {
        return srcAp;
    }

    @JsonProperty("src_ap")
    public void setSrcAp(String srcAp) {
        this.srcAp = srcAp;
    }

    @JsonProperty("src_port")
    public Integer getSrcPort() {
        return srcPort;
    }

    @JsonProperty("src_port")
    public void setSrcPort(Integer srcPort) {
        this.srcPort = srcPort;
    }

    @JsonProperty("tcp_ack")
    public Long getTcpAck() {
        return tcpAck;
    }

    @JsonProperty("tcp_ack")
    public void setTcpAck(Long tcpAck) {
        this.tcpAck = tcpAck;
    }

    @JsonProperty("tcp_flags")
    public String getTcpFlags() {
        return tcpFlags;
    }

    @JsonProperty("tcp_flags")
    public void setTcpFlags(String tcpFlags) {
        this.tcpFlags = tcpFlags;
    }

    @JsonProperty("tcp_len")
    public Integer getTcpLen() {
        return tcpLen;
    }

    @JsonProperty("tcp_len")
    public void setTcpLen(Integer tcpLen) {
        this.tcpLen = tcpLen;
    }

    @JsonProperty("tcp_seq")
    public Integer getTcpSeq() {
        return tcpSeq;
    }

    @JsonProperty("tcp_seq")
    public void setTcpSeq(Integer tcpSeq) {
        this.tcpSeq = tcpSeq;
    }

    @JsonProperty("tcp_win")
    public Integer getTcpWin() {
        return tcpWin;
    }

    @JsonProperty("tcp_win")
    public void setTcpWin(Integer tcpWin) {
        this.tcpWin = tcpWin;
    }

    @JsonProperty("tos")
    public Integer getTos() {
        return tos;
    }

    @JsonProperty("tos")
    public void setTos(Integer tos) {
        this.tos = tos;
    }

    @JsonProperty("ttl")
    public Integer getTtl() {
        return ttl;
    }

    @JsonProperty("ttl")
    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    @JsonProperty("vlan")
    public Integer getVlan() {
        return vlan;
    }

    @JsonProperty("vlan")
    public void setVlan(Integer vlan) {
        this.vlan = vlan;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Event.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("seconds");
        sb.append('=');
        sb.append(((this.seconds == null)?"<null>":this.seconds));
        sb.append(',');
        sb.append("action");
        sb.append('=');
        sb.append(((this.action == null)?"<null>":this.action));
        sb.append(',');
        sb.append("_class");
        sb.append('=');
        sb.append(((this._class == null)?"<null>":this._class));
        sb.append(',');
        sb.append("dir");
        sb.append('=');
        sb.append(((this.dir == null)?"<null>":this.dir));
        sb.append(',');
        sb.append("dstAddr");
        sb.append('=');
        sb.append(((this.dstAddr == null)?"<null>":this.dstAddr));
        sb.append(',');
        sb.append("dstAp");
        sb.append('=');
        sb.append(((this.dstAp == null)?"<null>":this.dstAp));
        sb.append(',');
        sb.append("dstPort");
        sb.append('=');
        sb.append(((this.dstPort == null)?"<null>":this.dstPort));
        sb.append(',');
        sb.append("ethDst");
        sb.append('=');
        sb.append(((this.ethDst == null)?"<null>":this.ethDst));
        sb.append(',');
        sb.append("ethLen");
        sb.append('=');
        sb.append(((this.ethLen == null)?"<null>":this.ethLen));
        sb.append(',');
        sb.append("ethSrc");
        sb.append('=');
        sb.append(((this.ethSrc == null)?"<null>":this.ethSrc));
        sb.append(',');
        sb.append("ethType");
        sb.append('=');
        sb.append(((this.ethType == null)?"<null>":this.ethType));
        sb.append(',');
        sb.append("gid");
        sb.append('=');
        sb.append(((this.gid == null)?"<null>":this.gid));
        sb.append(',');
        sb.append("iface");
        sb.append('=');
        sb.append(((this.iface == null)?"<null>":this.iface));
        sb.append(',');
        sb.append("ipId");
        sb.append('=');
        sb.append(((this.ipId == null)?"<null>":this.ipId));
        sb.append(',');
        sb.append("ipLen");
        sb.append('=');
        sb.append(((this.ipLen == null)?"<null>":this.ipLen));
        sb.append(',');
        sb.append("msg");
        sb.append('=');
        sb.append(((this.msg == null)?"<null>":this.msg));
        sb.append(',');
        sb.append("mpls");
        sb.append('=');
        sb.append(((this.mpls == null)?"<null>":this.mpls));
        sb.append(',');
        sb.append("pktGen");
        sb.append('=');
        sb.append(((this.pktGen == null)?"<null>":this.pktGen));
        sb.append(',');
        sb.append("pktLen");
        sb.append('=');
        sb.append(((this.pktLen == null)?"<null>":this.pktLen));
        sb.append(',');
        sb.append("pktNum");
        sb.append('=');
        sb.append(((this.pktNum == null)?"<null>":this.pktNum));
        sb.append(',');
        sb.append("priority");
        sb.append('=');
        sb.append(((this.priority == null)?"<null>":this.priority));
        sb.append(',');
        sb.append("proto");
        sb.append('=');
        sb.append(((this.proto == null)?"<null>":this.proto));
        sb.append(',');
        sb.append("rev");
        sb.append('=');
        sb.append(((this.rev == null)?"<null>":this.rev));
        sb.append(',');
        sb.append("rule");
        sb.append('=');
        sb.append(((this.rule == null)?"<null>":this.rule));
        sb.append(',');
        sb.append("service");
        sb.append('=');
        sb.append(((this.service == null)?"<null>":this.service));
        sb.append(',');
        sb.append("sid");
        sb.append('=');
        sb.append(((this.sid == null)?"<null>":this.sid));
        sb.append(',');
        sb.append("srcAddr");
        sb.append('=');
        sb.append(((this.srcAddr == null)?"<null>":this.srcAddr));
        sb.append(',');
        sb.append("srcAp");
        sb.append('=');
        sb.append(((this.srcAp == null)?"<null>":this.srcAp));
        sb.append(',');
        sb.append("srcPort");
        sb.append('=');
        sb.append(((this.srcPort == null)?"<null>":this.srcPort));
        sb.append(',');
        sb.append("tcpAck");
        sb.append('=');
        sb.append(((this.tcpAck == null)?"<null>":this.tcpAck));
        sb.append(',');
        sb.append("tcpFlags");
        sb.append('=');
        sb.append(((this.tcpFlags == null)?"<null>":this.tcpFlags));
        sb.append(',');
        sb.append("tcpLen");
        sb.append('=');
        sb.append(((this.tcpLen == null)?"<null>":this.tcpLen));
        sb.append(',');
        sb.append("tcpSeq");
        sb.append('=');
        sb.append(((this.tcpSeq == null)?"<null>":this.tcpSeq));
        sb.append(',');
        sb.append("tcpWin");
        sb.append('=');
        sb.append(((this.tcpWin == null)?"<null>":this.tcpWin));
        sb.append(',');
        sb.append("tos");
        sb.append('=');
        sb.append(((this.tos == null)?"<null>":this.tos));
        sb.append(',');
        sb.append("ttl");
        sb.append('=');
        sb.append(((this.ttl == null)?"<null>":this.ttl));
        sb.append(',');
        sb.append("vlan");
        sb.append('=');
        sb.append(((this.vlan == null)?"<null>":this.vlan));
        sb.append(',');
        sb.append("timestamp");
        sb.append('=');
        sb.append(((this.timestamp == null)?"<null>":this.timestamp));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}