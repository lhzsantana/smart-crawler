package info.trintaetres.smart_crawler.indexer;

public class GeneralCassandra {
	
	/*

	private static final Logger logger = LoggerFactory
			.getLogger(BulkIndexer.class);
	private static Keyspace keyspace = null;
	private static Utils utils = Utils.getInstance();

	public static final ColumnFamily CF_COUNTER1 = new ColumnFamily<Long, String>(
			"CounterColumnFamily", LongSerializer.get(), StringSerializer.get());

	public static Keyspace getKeyspace() {

		if (keyspace == null) {
			/*
			AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
					.forCluster("ClusterName")
					.forKeyspace("KeyspaceName")
					.withAstyanaxConfiguration(
							new AstyanaxConfigurationImpl().setDiscoveryType(
									NodeDiscoveryType.RING_DESCRIBE)
									.setConnectionPoolType(
											ConnectionPoolType.TOKEN_AWARE))
					.withConnectionPoolConfiguration(
							new ConnectionPoolConfigurationImpl(
									"MyConnectionPool").setPort(9160)
									.setMaxConnsPerHost(3)
									.setSeeds("127.0.0.1:9160"))
					.withConnectionPoolMonitor(
							new CountingConnectionPoolMonitor())
					.buildKeyspace(ThriftFamilyFactory.getInstance());

			context.start();
			keyspace = context.getClient();
		}

		return keyspace;
	}

	public void createCounter() {
	}

	public void incrementCounter(String word) throws ConnectionException {
		keyspace.prepareColumnMutation(CF_COUNTER1, word, "CounterColumn1")
				.incrementCounterColumn(1).execute();
	}

	public long getCounter(String word) throws ConnectionException {

		Column<String> result = (Column<String>) keyspace.prepareQuery(CF_COUNTER1)
				.getKey(word).getColumn("Column1").execute().getResult();
		return result.getLongValue();
	}

			*/
}
