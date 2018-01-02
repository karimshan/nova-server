package org.nova.network;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.nova.Constants;
import org.nova.game.engine.cores.CoresManager;
import org.nova.network.decoders.packets.PacketDecoder;
import org.nova.network.stream.InputStream;

public final class ServerChannelHandler extends SimpleChannelHandler {

	private static ChannelGroup channels;
	private static ServerBootstrap bootstrap;

	public static final void init() {
		new ServerChannelHandler();
	}

	public static int getConnectedChannelsSize() {
		return channels == null ? 0 : channels.size();
	}
	
	private static Channel channel;
	
	public static Channel getChannel() {
		return channel;
	}
	
	public static void setChannel(Channel c) {
		channel = c;
	}

	/*
	 * throws exeption so if cant handle channel server closes
	 */
	private ServerChannelHandler() {
		channels = new DefaultChannelGroup();
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				CoresManager.serverBossChannelExecutor,
				CoresManager.serverWorkerChannelExecutor,
				CoresManager.serverWorkersCount));
		bootstrap.getPipeline().addLast("handler", this);
		bootstrap.setOption("reuseAddress", true); // re-uses address for bind
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.TcpAckFrequency", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(Constants.PORT_ID));
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		setChannel(e.getChannel());
		channels.add(e.getChannel());
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		channels.remove(e.getChannel());
		//System.out.println("Closed channel - ChannelClosed");
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		ctx.setAttachment(new Session(e.getChannel()));
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) {
		Object sessionObject = ctx.getAttachment();
		if (sessionObject != null && sessionObject instanceof Session) {
			Session session = (Session) sessionObject;
			if (session.getDecoder() == null)
				return;
			if (session.getDecoder() instanceof PacketDecoder)
				session.getWorldPackets().getPlayer().finish();
		}
		//System.out.println("Closed channel - CHANNEL DISCONNECTED");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (!(e.getMessage() instanceof ChannelBuffer))
			return;
		Object sessionObject = ctx.getAttachment();
		if (sessionObject != null && sessionObject instanceof Session) {
			Session session = (Session) sessionObject;
			if (session.getDecoder() == null)
				return;
			ChannelBuffer buf = (ChannelBuffer) e.getMessage();
			buf.markReaderIndex();
			int avail = buf.readableBytes();
			if (avail < 1 || avail > Constants.RECEIVE_DATA_LIMIT)
				return;
			byte[] buffer = new byte[avail];
			buf.readBytes(buffer);
			try {
				session.getDecoder().decode(new InputStream(buffer));
			} catch (Throwable er) {
				er.printStackTrace();
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ee)
			throws Exception {

	}

	public static final void shutdown() {
		channels.close().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}

}
