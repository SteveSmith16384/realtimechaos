package chaosrt.server;

import java.io.*;

import chaosrt.Server;

public class ServerMountedSkeleton extends ServerPerson {

  public ServerMountedSkeleton(double x, double y, int side, boolean ill) throws IOException {
    super("Mounted Skeleton", Server.MOUNTED_SKELETON, x, y, side, ill);
  }

}
